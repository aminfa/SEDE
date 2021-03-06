from exe.data import SEDEObject, ServiceInstance, ServiceInstanceHandle
from exe.execution import ExecutionEnvironment
from exe.execution import Task
from procedure import Procedure
from util import reflector
from util.reflector import traverse_package as tp
import uuid



class InstructionProcedure(Procedure):

    def wrap_into_servicehandle(self, instance, executorId, classpath):
        instance_id = uuid.uuid4().hex[0:10]
        result = ServiceInstance(executorId=executorId, classpath=classpath, id=instance_id)
        result.service_instance = instance
        return result

    def process_task(self, task: Task):
        env: ExecutionEnvironment = task.execution.env
        # gather inputs:
        input_fields = list()
        for input_fieldname in task["params"]:
            input_field = env[input_fieldname].data
            input_fields.append(input_field)

        result = None
        result_type = task["leftsidefieldtype"]
        result_class :str = task["leftsidefieldclass"]
        # to_call
        if task["is-service-construction"]:
            assert not task["is-context-a-fieldname"]
            classpath = task["context"]
            instance = reflector.construct(classpath, *input_fields)
            result = self.wrap_into_servicehandle(instance, task.execution.config.executor_id, result_type)
            result_type = ServiceInstanceHandle.__name__

        else:
            method = task["method"]
            if task["is-context-a-fieldname"]:
                service_fieldname = task["context"]
                field:SEDEObject = env[service_fieldname]
                assert field.is_service_instance()
                serviceinstance: ServiceInstance = field.data
                result = reflector.call(serviceinstance.service_instance, method, *input_fields)
            else:
                classpath = task["context"]
                context = tp(classpath)
                result = reflector.call(context, method, *input_fields)
                if result_class is not None and result_class.startswith("ServiceInstance"):
                    # this method creates a service instance:
                    result = self.wrap_into_servicehandle(result, task.execution.config.executor_id, result_type)
                    result_type = ServiceInstanceHandle.__name__


        leftside_fieldname = task["leftsidefieldname"]
        if leftside_fieldname is not None:
            output_index = task["output-index"]
            if output_index == -1:
                output_obj = SEDEObject(result_type, result)
            else:
                param_fieldname = task["params"][output_index]
                output_obj = env[param_fieldname]
            env[leftside_fieldname] = output_obj
        task.set_succeeded()

