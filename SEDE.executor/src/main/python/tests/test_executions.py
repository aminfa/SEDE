# Executes execRequests generated by PlainLibGraphs.java.

from exe.executor import Executor
from exe.config import ExecutorConfig
from exe.requests import ExecRequest, DataPutRequest
from exe.data import ServiceInstanceHandle

from time import sleep


with open("testrsc/deployment/config.json", "r") as configfile:
    config = ExecutorConfig.from_json_string(configfile.read())

executor = Executor(config)

root_executions = "testrsc/exec-requests/plainlib/"
def read_resource(filename)->str:
    with open(root_executions + filename, "r") as executefile:
        return executefile.read()

exec_1 = ExecRequest.from_json_string(read_resource("e_1.json"))

execution_1 = executor.execute(exec_1)

while executor.execPool.execIdTaken(exec_1.requestId):
    sleep(1)
print("done execution 1")

assert "b" in execution_1.env
b: ServiceInstanceHandle = execution_1.env["b"].data

json_string = read_resource("e_2.json")
json_string = json_string.replace("$INSTANCE_ID b", b.serviceId)
exec_2 = ExecRequest.from_json_string(json_string)

execution_2 = executor.execute(exec_2)

put_request_2_b : DataPutRequest = DataPutRequest(requestId=execution_2.exec_id, fieldname="b", data=b)
executor.put(put_request_2_b)

while executor.execPool.execIdTaken(exec_2.requestId):
    sleep(1)
print("done execution 2")
state1 = execution_2.env["state1"].data
state2 = execution_2.env["state2"].data

assert state1 == "a = 0 b = 1"
assert state2 == "a = 0 b = -1"