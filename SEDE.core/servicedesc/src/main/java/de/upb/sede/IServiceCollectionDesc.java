package de.upb.sede;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.upb.sede.exec.IServiceDesc;
import de.upb.sede.types.IDataTypeDesc;
import org.immutables.value.Value;

import java.util.List;

@SModelStyle
@Value.Immutable
@Value.Modifiable
@JsonDeserialize(builder = ServiceCollectionDesc.Builder.class)
public interface IServiceCollectionDesc extends ICommented, IQualifiable {

    List<IServiceDesc> getServices();

    List<IDataTypeDesc> getDataTypes();

    // TODO add deployments
}