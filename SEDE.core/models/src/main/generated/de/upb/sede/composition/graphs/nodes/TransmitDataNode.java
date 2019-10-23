package de.upb.sede.composition.graphs.nodes;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.Var;
import de.upb.sede.IFieldContainer;
import de.upb.sede.exec.IExecutorContactInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;
import org.immutables.value.Generated;

/**
 * Immutable implementation of {@link ITransmitDataNode}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code TransmitDataNode.builder()}.
 */
@Generated(from = "ITransmitDataNode", generator = "Immutables")
@SuppressWarnings({"all"})
@ParametersAreNonnullByDefault
@javax.annotation.Generated("org.immutables.processor.ProxyProcessor")
@Immutable
@CheckReturnValue
public final class TransmitDataNode implements ITransmitDataNode {
  private final IExecutorContactInfo contactInfo;
  private final @Nullable String caster;
  private final @Nullable String semantiveType;
  private final String nodeType;
  private final String fieldName;

  private TransmitDataNode(TransmitDataNode.Builder builder) {
    this.contactInfo = builder.contactInfo;
    this.caster = builder.caster;
    this.semantiveType = builder.semantiveType;
    this.fieldName = builder.fieldName;
    this.nodeType = builder.nodeType != null
        ? builder.nodeType
        : Objects.requireNonNull(ITransmitDataNode.super.getNodeType(), "nodeType");
  }

  private TransmitDataNode(
      IExecutorContactInfo contactInfo,
      @Nullable String caster,
      @Nullable String semantiveType,
      String nodeType,
      String fieldName) {
    this.contactInfo = contactInfo;
    this.caster = caster;
    this.semantiveType = semantiveType;
    this.nodeType = nodeType;
    this.fieldName = fieldName;
  }

  /**
   * @return The value of the {@code contactInfo} attribute
   */
  @JsonProperty("contactInfo")
  @Override
  public IExecutorContactInfo getContactInfo() {
    return contactInfo;
  }

  /**
   * @return The value of the {@code caster} attribute
   */
  @JsonProperty("caster")
  @Override
  public @Nullable String getCaster() {
    return caster;
  }

  /**
   * @return The value of the {@code semantiveType} attribute
   */
  @JsonProperty("semantiveType")
  @Override
  public @Nullable String getSemantiveType() {
    return semantiveType;
  }

  /**
   * @return The value of the {@code nodeType} attribute
   */
  @JsonProperty("nodeType")
  @Override
  public String getNodeType() {
    return nodeType;
  }

  /**
   * Returns the field name that is being refered at.
   * @return Referenced field name
   */
  @JsonProperty("fieldName")
  @Override
  public String getFieldName() {
    return fieldName;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link ITransmitDataNode#getContactInfo() contactInfo} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for contactInfo
   * @return A modified copy of the {@code this} object
   */
  public final TransmitDataNode withContactInfo(IExecutorContactInfo value) {
    if (this.contactInfo == value) return this;
    IExecutorContactInfo newValue = Objects.requireNonNull(value, "contactInfo");
    return new TransmitDataNode(newValue, this.caster, this.semantiveType, this.nodeType, this.fieldName);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link ITransmitDataNode#getCaster() caster} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for caster (can be {@code null})
   * @return A modified copy of the {@code this} object
   */
  public final TransmitDataNode withCaster(@Nullable String value) {
    if (Objects.equals(this.caster, value)) return this;
    return new TransmitDataNode(this.contactInfo, value, this.semantiveType, this.nodeType, this.fieldName);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link ITransmitDataNode#getSemantiveType() semantiveType} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for semantiveType (can be {@code null})
   * @return A modified copy of the {@code this} object
   */
  public final TransmitDataNode withSemantiveType(@Nullable String value) {
    if (Objects.equals(this.semantiveType, value)) return this;
    return new TransmitDataNode(this.contactInfo, this.caster, value, this.nodeType, this.fieldName);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link ITransmitDataNode#getNodeType() nodeType} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for nodeType
   * @return A modified copy of the {@code this} object
   */
  public final TransmitDataNode withNodeType(String value) {
    String newValue = Objects.requireNonNull(value, "nodeType");
    if (this.nodeType.equals(newValue)) return this;
    return new TransmitDataNode(this.contactInfo, this.caster, this.semantiveType, newValue, this.fieldName);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link ITransmitDataNode#getFieldName() fieldName} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for fieldName
   * @return A modified copy of the {@code this} object
   */
  public final TransmitDataNode withFieldName(String value) {
    String newValue = Objects.requireNonNull(value, "fieldName");
    if (this.fieldName.equals(newValue)) return this;
    return new TransmitDataNode(this.contactInfo, this.caster, this.semantiveType, this.nodeType, newValue);
  }

  /**
   * This instance is equal to all instances of {@code TransmitDataNode} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another) return true;
    return another instanceof TransmitDataNode
        && equalTo((TransmitDataNode) another);
  }

  private boolean equalTo(TransmitDataNode another) {
    return contactInfo.equals(another.contactInfo)
        && Objects.equals(caster, another.caster)
        && Objects.equals(semantiveType, another.semantiveType)
        && nodeType.equals(another.nodeType)
        && fieldName.equals(another.fieldName);
  }

  /**
   * Computes a hash code from attributes: {@code contactInfo}, {@code caster}, {@code semantiveType}, {@code nodeType}, {@code fieldName}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    @Var int h = 5381;
    h += (h << 5) + contactInfo.hashCode();
    h += (h << 5) + Objects.hashCode(caster);
    h += (h << 5) + Objects.hashCode(semantiveType);
    h += (h << 5) + nodeType.hashCode();
    h += (h << 5) + fieldName.hashCode();
    return h;
  }

  /**
   * Prints the immutable value {@code TransmitDataNode} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper("TransmitDataNode")
        .omitNullValues()
        .add("contactInfo", contactInfo)
        .add("caster", caster)
        .add("semantiveType", semantiveType)
        .add("nodeType", nodeType)
        .add("fieldName", fieldName)
        .toString();
  }

  /**
   * Utility type used to correctly read immutable object from JSON representation.
   * @deprecated Do not use this type directly, it exists only for the <em>Jackson</em>-binding infrastructure
   */
  @Generated(from = "ITransmitDataNode", generator = "Immutables")
  @Deprecated
  @SuppressWarnings("Immutable")
  @JsonDeserialize
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
  static final class Json implements ITransmitDataNode {
    @Nullable IExecutorContactInfo contactInfo;
    @Nullable String caster;
    @Nullable String semantiveType;
    @Nullable String nodeType;
    @Nullable String fieldName;
    @JsonProperty("contactInfo")
    public void setContactInfo(IExecutorContactInfo contactInfo) {
      this.contactInfo = contactInfo;
    }
    @JsonProperty("caster")
    public void setCaster(@Nullable String caster) {
      this.caster = caster;
    }
    @JsonProperty("semantiveType")
    public void setSemantiveType(@Nullable String semantiveType) {
      this.semantiveType = semantiveType;
    }
    @JsonProperty("nodeType")
    public void setNodeType(String nodeType) {
      this.nodeType = nodeType;
    }
    @JsonProperty("fieldName")
    public void setFieldName(String fieldName) {
      this.fieldName = fieldName;
    }
    @Override
    public IExecutorContactInfo getContactInfo() { throw new UnsupportedOperationException(); }
    @Override
    public String getCaster() { throw new UnsupportedOperationException(); }
    @Override
    public String getSemantiveType() { throw new UnsupportedOperationException(); }
    @Override
    public String getNodeType() { throw new UnsupportedOperationException(); }
    @Override
    public String getFieldName() { throw new UnsupportedOperationException(); }
  }

  /**
   * @param json A JSON-bindable data structure
   * @return An immutable value type
   * @deprecated Do not use this method directly, it exists only for the <em>Jackson</em>-binding infrastructure
   */
  @Deprecated
  @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
  static TransmitDataNode fromJson(Json json) {
    TransmitDataNode.Builder builder = TransmitDataNode.builder();
    if (json.contactInfo != null) {
      builder.contactInfo(json.contactInfo);
    }
    if (json.caster != null) {
      builder.caster(json.caster);
    }
    if (json.semantiveType != null) {
      builder.semantiveType(json.semantiveType);
    }
    if (json.nodeType != null) {
      builder.nodeType(json.nodeType);
    }
    if (json.fieldName != null) {
      builder.fieldName(json.fieldName);
    }
    return builder.build();
  }

  /**
   * Creates an immutable copy of a {@link ITransmitDataNode} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable TransmitDataNode instance
   */
  public static TransmitDataNode copyOf(ITransmitDataNode instance) {
    if (instance instanceof TransmitDataNode) {
      return (TransmitDataNode) instance;
    }
    return TransmitDataNode.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link TransmitDataNode TransmitDataNode}.
   * <pre>
   * TransmitDataNode.builder()
   *    .contactInfo(de.upb.sede.exec.IExecutorContactInfo) // required {@link ITransmitDataNode#getContactInfo() contactInfo}
   *    .caster(String | null) // nullable {@link ITransmitDataNode#getCaster() caster}
   *    .semantiveType(String | null) // nullable {@link ITransmitDataNode#getSemantiveType() semantiveType}
   *    .nodeType(String) // optional {@link ITransmitDataNode#getNodeType() nodeType}
   *    .fieldName(String) // required {@link ITransmitDataNode#getFieldName() fieldName}
   *    .build();
   * </pre>
   * @return A new TransmitDataNode builder
   */
  public static TransmitDataNode.Builder builder() {
    return new TransmitDataNode.Builder();
  }

  /**
   * Builds instances of type {@link TransmitDataNode TransmitDataNode}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  @Generated(from = "ITransmitDataNode", generator = "Immutables")
  @NotThreadSafe
  public static final class Builder {
    private static final long INIT_BIT_CONTACT_INFO = 0x1L;
    private static final long INIT_BIT_FIELD_NAME = 0x2L;
    private long initBits = 0x3L;

    private @Nullable IExecutorContactInfo contactInfo;
    private @Nullable String caster;
    private @Nullable String semantiveType;
    private @Nullable String nodeType;
    private @Nullable String fieldName;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code MutableTransmitDataNode} instance.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    @CanIgnoreReturnValue 
    public final Builder from(MutableTransmitDataNode instance) {
      Objects.requireNonNull(instance, "instance");
      if (instance.contactInfoIsSet()) {
        contactInfo(instance.getContactInfo());
      }
      @Nullable String casterValue = instance.getCaster();
      if (casterValue != null) {
        caster(casterValue);
      }
      @Nullable String semantiveTypeValue = instance.getSemantiveType();
      if (semantiveTypeValue != null) {
        semantiveType(semantiveTypeValue);
      }
      nodeType(instance.getNodeType());
      if (instance.fieldNameIsSet()) {
        fieldName(instance.getFieldName());
      }
      return this;
    }

    /**
     * Fill a builder with attribute values from the provided {@code de.upb.sede.IFieldContainer} instance.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    @CanIgnoreReturnValue 
    public final Builder from(IFieldContainer instance) {
      Objects.requireNonNull(instance, "instance");
      from((Object) instance);
      return this;
    }

    /**
     * Fill a builder with attribute values from the provided {@code de.upb.sede.composition.graphs.nodes.BaseNode} instance.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    @CanIgnoreReturnValue 
    public final Builder from(BaseNode instance) {
      Objects.requireNonNull(instance, "instance");
      from((Object) instance);
      return this;
    }

    /**
     * Fill a builder with attribute values from the provided {@code de.upb.sede.composition.graphs.nodes.ITransmitDataNode} instance.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    @CanIgnoreReturnValue 
    public final Builder from(ITransmitDataNode instance) {
      Objects.requireNonNull(instance, "instance");
      from((Object) instance);
      return this;
    }

    private void from(Object object) {
      if (object instanceof MutableTransmitDataNode) {
        from((MutableTransmitDataNode) object);
        return;
      }
      if (object instanceof IFieldContainer) {
        IFieldContainer instance = (IFieldContainer) object;
        fieldName(instance.getFieldName());
      }
      if (object instanceof BaseNode) {
        BaseNode instance = (BaseNode) object;
        nodeType(instance.getNodeType());
      }
      if (object instanceof ITransmitDataNode) {
        ITransmitDataNode instance = (ITransmitDataNode) object;
        @Nullable String casterValue = instance.getCaster();
        if (casterValue != null) {
          caster(casterValue);
        }
        contactInfo(instance.getContactInfo());
        @Nullable String semantiveTypeValue = instance.getSemantiveType();
        if (semantiveTypeValue != null) {
          semantiveType(semantiveTypeValue);
        }
      }
    }

    /**
     * Initializes the value for the {@link ITransmitDataNode#getContactInfo() contactInfo} attribute.
     * @param contactInfo The value for contactInfo 
     * @return {@code this} builder for use in a chained invocation
     */
    @CanIgnoreReturnValue 
    @JsonProperty("contactInfo")
    public final Builder contactInfo(IExecutorContactInfo contactInfo) {
      this.contactInfo = Objects.requireNonNull(contactInfo, "contactInfo");
      initBits &= ~INIT_BIT_CONTACT_INFO;
      return this;
    }

    /**
     * Initializes the value for the {@link ITransmitDataNode#getCaster() caster} attribute.
     * @param caster The value for caster (can be {@code null})
     * @return {@code this} builder for use in a chained invocation
     */
    @CanIgnoreReturnValue 
    @JsonProperty("caster")
    public final Builder caster(@Nullable String caster) {
      this.caster = caster;
      return this;
    }

    /**
     * Initializes the value for the {@link ITransmitDataNode#getSemantiveType() semantiveType} attribute.
     * @param semantiveType The value for semantiveType (can be {@code null})
     * @return {@code this} builder for use in a chained invocation
     */
    @CanIgnoreReturnValue 
    @JsonProperty("semantiveType")
    public final Builder semantiveType(@Nullable String semantiveType) {
      this.semantiveType = semantiveType;
      return this;
    }

    /**
     * Initializes the value for the {@link ITransmitDataNode#getNodeType() nodeType} attribute.
     * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link ITransmitDataNode#getNodeType() nodeType}.</em>
     * @param nodeType The value for nodeType 
     * @return {@code this} builder for use in a chained invocation
     */
    @CanIgnoreReturnValue 
    @JsonProperty("nodeType")
    public final Builder nodeType(String nodeType) {
      this.nodeType = Objects.requireNonNull(nodeType, "nodeType");
      return this;
    }

    /**
     * Initializes the value for the {@link ITransmitDataNode#getFieldName() fieldName} attribute.
     * @param fieldName The value for fieldName 
     * @return {@code this} builder for use in a chained invocation
     */
    @CanIgnoreReturnValue 
    @JsonProperty("fieldName")
    public final Builder fieldName(String fieldName) {
      this.fieldName = Objects.requireNonNull(fieldName, "fieldName");
      initBits &= ~INIT_BIT_FIELD_NAME;
      return this;
    }

    /**
     * Builds a new {@link TransmitDataNode TransmitDataNode}.
     * @return An immutable instance of TransmitDataNode
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public TransmitDataNode build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new TransmitDataNode(this);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<>();
      if ((initBits & INIT_BIT_CONTACT_INFO) != 0) attributes.add("contactInfo");
      if ((initBits & INIT_BIT_FIELD_NAME) != 0) attributes.add("fieldName");
      return "Cannot build TransmitDataNode, some of required attributes are not set " + attributes;
    }
  }
}
