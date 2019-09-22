package de.upb.sede.exec.aux;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.upb.sede.SModelStyle;
import org.immutables.value.Value;

@SModelStyle
@Value.Immutable
@Value.Modifiable
@JsonDeserialize(builder = PythonMethodAux.Builder.class)
public interface IPythonMethodAux {
}