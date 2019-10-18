package de.upb.sede.param.auxiliary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.primitives.Booleans;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import de.upb.sede.exec.auxiliary.IJavaDispatchAux;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;
import org.immutables.value.Generated;

/**
 * A modifiable implementation of the {@link IJavaParameterizationAux IJavaParameterizationAux} type.
 * <p>Use the {@link #create()} static factory methods to create new instances.
 * Use the {@link #toImmutable()} method to convert to canonical immutable instances.
 * <p><em>MutableJavaParameterizationAux is not thread-safe</em>
 * @see JavaParameterizationAux
 */
@Generated(from = "IJavaParameterizationAux", generator = "Modifiables")
@SuppressWarnings({"all"})
@ParametersAreNonnullByDefault
@javax.annotation.Generated({"Modifiables.generator", "IJavaParameterizationAux"})
@NotThreadSafe
public final class MutableJavaParameterizationAux
    implements IJavaParameterizationAux {
  private static final long OPT_BIT_AUTO_SCAN_EACH_PARAM = 0x1L;
  private static final long OPT_BIT_BUNDLE_IN_MAP = 0x2L;
  private static final long OPT_BIT_BUNDLE_IN_ARRAY = 0x4L;
  private static final long OPT_BIT_BUNDLE_IN_LIST = 0x8L;
  private static final long OPT_BIT_PRECEDE_PARAMS_WITH_NAMES = 0x10L;
  private long optBits;

  private @Nullable IJavaDispatchAux parameterHandler;
  private boolean autoScanEachParam;
  private boolean bundleInMap;
  private boolean bundleInArray;
  private boolean bundleInList;
  private boolean precedeParamsWithNames;
  private ArrayList<String> paramOrder = null;

  private MutableJavaParameterizationAux() {}

  /**
   * Construct a modifiable instance of {@code IJavaParameterizationAux}.
   * @return A new modifiable instance
   */
  public static MutableJavaParameterizationAux create() {
    return new MutableJavaParameterizationAux();
  }

  /**
   * @return value of {@code parameterHandler} attribute, may be {@code null}
   */
  @JsonProperty("parameterHandler")
  @Override
  public final @Nullable IJavaDispatchAux getParameterHandler() {
    return parameterHandler;
  }

  /**
   * @return assigned or, otherwise, newly computed, not cached value of {@code autoScanEachParam} attribute
   */
  @JsonProperty("autoScanEachParam")
  @Override
  public final boolean getAutoScanEachParam() {
    return autoScanEachParamIsSet()
        ? autoScanEachParam
        : IJavaParameterizationAux.super.getAutoScanEachParam();
  }

  /**
   * @return assigned or, otherwise, newly computed, not cached value of {@code bundleInMap} attribute
   */
  @JsonProperty("bundleInMap")
  @Override
  public final boolean getBundleInMap() {
    return bundleInMapIsSet()
        ? bundleInMap
        : IJavaParameterizationAux.super.getBundleInMap();
  }

  /**
   * @return assigned or, otherwise, newly computed, not cached value of {@code bundleInArray} attribute
   */
  @JsonProperty("bundleInArray")
  @Override
  public final boolean getBundleInArray() {
    return bundleInArrayIsSet()
        ? bundleInArray
        : IJavaParameterizationAux.super.getBundleInArray();
  }

  /**
   * @return assigned or, otherwise, newly computed, not cached value of {@code bundleInList} attribute
   */
  @JsonProperty("bundleInList")
  @Override
  public final boolean getBundleInList() {
    return bundleInListIsSet()
        ? bundleInList
        : IJavaParameterizationAux.super.getBundleInList();
  }

  /**
   * @return assigned or, otherwise, newly computed, not cached value of {@code precedeParamsWithNames} attribute
   */
  @JsonProperty("precedeParamsWithNames")
  @Override
  public final boolean getPrecedeParamsWithNames() {
    return precedeParamsWithNamesIsSet()
        ? precedeParamsWithNames
        : IJavaParameterizationAux.super.getPrecedeParamsWithNames();
  }

  /**
   * @return modifiable list {@code paramOrder}
   */
  @JsonProperty("paramOrder")
  @Override
  public final @Nullable List<String> getParamOrder() {
    return paramOrder;
  }

  /**
   * Clears the object by setting all attributes to their initial values.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public MutableJavaParameterizationAux clear() {
    optBits = 0;
    parameterHandler = null;
    autoScanEachParam = false;
    bundleInMap = false;
    bundleInArray = false;
    bundleInList = false;
    precedeParamsWithNames = false;
    paramOrder = null;
    return this;
  }

  /**
   * Fill this modifiable instance with attribute values from the provided {@link IJavaParameterizationAux} instance.
   * Regular attribute values will be overridden, i.e. replaced with ones of an instance.
   * Any of the instance's absent optional values will not be copied (will not override current values).
   * Collection elements and entries will be added, not replaced.
   * @param instance The instance from which to copy values
   * @return {@code this} for use in a chained invocation
   */
  public MutableJavaParameterizationAux from(IJavaParameterizationAux instance) {
    Objects.requireNonNull(instance, "instance");
    if (instance instanceof MutableJavaParameterizationAux) {
      from((MutableJavaParameterizationAux) instance);
      return this;
    }
    @Nullable IJavaDispatchAux parameterHandlerValue = instance.getParameterHandler();
    if (parameterHandlerValue != null) {
      setParameterHandler(parameterHandlerValue);
    }
    setAutoScanEachParam(instance.getAutoScanEachParam());
    setBundleInMap(instance.getBundleInMap());
    setBundleInArray(instance.getBundleInArray());
    setBundleInList(instance.getBundleInList());
    setPrecedeParamsWithNames(instance.getPrecedeParamsWithNames());
    addAllParamOrder(instance.getParamOrder());
    return this;
  }

  /**
   * Fill this modifiable instance with attribute values from the provided {@link IJavaParameterizationAux} instance.
   * Regular attribute values will be overridden, i.e. replaced with ones of an instance.
   * Any of the instance's absent optional values will not be copied (will not override current values).
   * Collection elements and entries will be added, not replaced.
   * @param instance The instance from which to copy values
   * @return {@code this} for use in a chained invocation
   */
  public MutableJavaParameterizationAux from(MutableJavaParameterizationAux instance) {
    Objects.requireNonNull(instance, "instance");
    @Nullable IJavaDispatchAux parameterHandlerValue = instance.getParameterHandler();
    if (parameterHandlerValue != null) {
      setParameterHandler(parameterHandlerValue);
    }
    setAutoScanEachParam(instance.getAutoScanEachParam());
    setBundleInMap(instance.getBundleInMap());
    setBundleInArray(instance.getBundleInArray());
    setBundleInList(instance.getBundleInList());
    setPrecedeParamsWithNames(instance.getPrecedeParamsWithNames());
    addAllParamOrder(instance.getParamOrder());
    return this;
  }

  /**
   * Assigns a value to the {@link IJavaParameterizationAux#getParameterHandler() parameterHandler} attribute.
   * @param parameterHandler The value for parameterHandler, can be {@code null}
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public MutableJavaParameterizationAux setParameterHandler(@Nullable IJavaDispatchAux parameterHandler) {
    this.parameterHandler = parameterHandler;
    return this;
  }

  /**
   * Assigns a value to the {@link IJavaParameterizationAux#getAutoScanEachParam() autoScanEachParam} attribute.
   * <p><em>If not set, this attribute will have a default value returned by the initializer of {@link IJavaParameterizationAux#getAutoScanEachParam() autoScanEachParam}.</em>
   * @param autoScanEachParam The value for autoScanEachParam
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public MutableJavaParameterizationAux setAutoScanEachParam(boolean autoScanEachParam) {
    this.autoScanEachParam = autoScanEachParam;
    optBits |= OPT_BIT_AUTO_SCAN_EACH_PARAM;
    return this;
  }

  /**
   * Assigns a value to the {@link IJavaParameterizationAux#getBundleInMap() bundleInMap} attribute.
   * <p><em>If not set, this attribute will have a default value returned by the initializer of {@link IJavaParameterizationAux#getBundleInMap() bundleInMap}.</em>
   * @param bundleInMap The value for bundleInMap
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public MutableJavaParameterizationAux setBundleInMap(boolean bundleInMap) {
    this.bundleInMap = bundleInMap;
    optBits |= OPT_BIT_BUNDLE_IN_MAP;
    return this;
  }

  /**
   * Assigns a value to the {@link IJavaParameterizationAux#getBundleInArray() bundleInArray} attribute.
   * <p><em>If not set, this attribute will have a default value returned by the initializer of {@link IJavaParameterizationAux#getBundleInArray() bundleInArray}.</em>
   * @param bundleInArray The value for bundleInArray
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public MutableJavaParameterizationAux setBundleInArray(boolean bundleInArray) {
    this.bundleInArray = bundleInArray;
    optBits |= OPT_BIT_BUNDLE_IN_ARRAY;
    return this;
  }

  /**
   * Assigns a value to the {@link IJavaParameterizationAux#getBundleInList() bundleInList} attribute.
   * <p><em>If not set, this attribute will have a default value returned by the initializer of {@link IJavaParameterizationAux#getBundleInList() bundleInList}.</em>
   * @param bundleInList The value for bundleInList
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public MutableJavaParameterizationAux setBundleInList(boolean bundleInList) {
    this.bundleInList = bundleInList;
    optBits |= OPT_BIT_BUNDLE_IN_LIST;
    return this;
  }

  /**
   * Assigns a value to the {@link IJavaParameterizationAux#getPrecedeParamsWithNames() precedeParamsWithNames} attribute.
   * <p><em>If not set, this attribute will have a default value returned by the initializer of {@link IJavaParameterizationAux#getPrecedeParamsWithNames() precedeParamsWithNames}.</em>
   * @param precedeParamsWithNames The value for precedeParamsWithNames
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public MutableJavaParameterizationAux setPrecedeParamsWithNames(boolean precedeParamsWithNames) {
    this.precedeParamsWithNames = precedeParamsWithNames;
    optBits |= OPT_BIT_PRECEDE_PARAMS_WITH_NAMES;
    return this;
  }

  /**
   * Adds one element to {@link IJavaParameterizationAux#getParamOrder() paramOrder} list.
   * @param element The paramOrder element
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public MutableJavaParameterizationAux addParamOrder(String element) {
    if (this.paramOrder == null) {
      this.
  paramOrder = new ArrayList<String>();
    }
    Objects.requireNonNull(element, "paramOrder element");
    this.paramOrder.add(element);
    return this;
  }

  /**
   * Adds elements to {@link IJavaParameterizationAux#getParamOrder() paramOrder} list.
   * @param elements An array of paramOrder elements
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final MutableJavaParameterizationAux addParamOrder(String... elements) {
    for (String e : elements) {
      addParamOrder(e);
    }
    return this;
  }

  /**
   * Sets or replaces all elements for {@link IJavaParameterizationAux#getParamOrder() paramOrder} list.
   * @param elements An iterable of paramOrder elements, can be {@code null}
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public MutableJavaParameterizationAux setParamOrder(@Nullable Iterable<String> elements) {
    if (elements == null) {
      this.paramOrder = null;
      return this;
    }
    if (this.paramOrder == null) {
      this.
  paramOrder = new ArrayList<String>();
    } else {
      this.paramOrder.clear();
    }
    addAllParamOrder(elements);
    return this;
  }

  /**
   * Adds elements to {@link IJavaParameterizationAux#getParamOrder() paramOrder} list.
   * @param elements An iterable of paramOrder elements
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public MutableJavaParameterizationAux addAllParamOrder(Iterable<String> elements) {
    if (elements == null) return this;
    if (this.paramOrder == null) {
      this.
  paramOrder = new ArrayList<String>();
    }
    for (String e : elements) {
      addParamOrder(e);
    }
    return this;
  }

  /**
   * Returns {@code true} if the default attribute {@link IJavaParameterizationAux#getAutoScanEachParam() autoScanEachParam} is set.
   * @return {@code true} if set
   */
  public final boolean autoScanEachParamIsSet() {
    return (optBits & OPT_BIT_AUTO_SCAN_EACH_PARAM) != 0;
  }

  /**
   * Returns {@code true} if the default attribute {@link IJavaParameterizationAux#getBundleInMap() bundleInMap} is set.
   * @return {@code true} if set
   */
  public final boolean bundleInMapIsSet() {
    return (optBits & OPT_BIT_BUNDLE_IN_MAP) != 0;
  }

  /**
   * Returns {@code true} if the default attribute {@link IJavaParameterizationAux#getBundleInArray() bundleInArray} is set.
   * @return {@code true} if set
   */
  public final boolean bundleInArrayIsSet() {
    return (optBits & OPT_BIT_BUNDLE_IN_ARRAY) != 0;
  }

  /**
   * Returns {@code true} if the default attribute {@link IJavaParameterizationAux#getBundleInList() bundleInList} is set.
   * @return {@code true} if set
   */
  public final boolean bundleInListIsSet() {
    return (optBits & OPT_BIT_BUNDLE_IN_LIST) != 0;
  }

  /**
   * Returns {@code true} if the default attribute {@link IJavaParameterizationAux#getPrecedeParamsWithNames() precedeParamsWithNames} is set.
   * @return {@code true} if set
   */
  public final boolean precedeParamsWithNamesIsSet() {
    return (optBits & OPT_BIT_PRECEDE_PARAMS_WITH_NAMES) != 0;
  }

  /**
   * Reset an attribute to its initial value.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final MutableJavaParameterizationAux unsetAutoScanEachParam() {
    optBits |= 0;
    autoScanEachParam = false;
    return this;
  }
  /**
   * Reset an attribute to its initial value.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final MutableJavaParameterizationAux unsetBundleInMap() {
    optBits |= 0;
    bundleInMap = false;
    return this;
  }
  /**
   * Reset an attribute to its initial value.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final MutableJavaParameterizationAux unsetBundleInArray() {
    optBits |= 0;
    bundleInArray = false;
    return this;
  }
  /**
   * Reset an attribute to its initial value.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final MutableJavaParameterizationAux unsetBundleInList() {
    optBits |= 0;
    bundleInList = false;
    return this;
  }
  /**
   * Reset an attribute to its initial value.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final MutableJavaParameterizationAux unsetPrecedeParamsWithNames() {
    optBits |= 0;
    precedeParamsWithNames = false;
    return this;
  }

  /**
   * Returns {@code true} if all required attributes are set, indicating that the object is initialized.
   * @return {@code true} if set
   */
  public final boolean isInitialized() {
    return true;
  }

  /**
   * Converts to {@link JavaParameterizationAux JavaParameterizationAux}.
   * @return An immutable instance of JavaParameterizationAux
   */
  public final JavaParameterizationAux toImmutable() {
    return JavaParameterizationAux.copyOf(this);
  }

  /**
   * This instance is equal to all instances of {@code MutableJavaParameterizationAux} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another) return true;
    if (!(another instanceof MutableJavaParameterizationAux)) return false;
    MutableJavaParameterizationAux other = (MutableJavaParameterizationAux) another;
    return equalTo(other);
  }

  private boolean equalTo(MutableJavaParameterizationAux another) {
    boolean autoScanEachParam = getAutoScanEachParam();
    boolean bundleInMap = getBundleInMap();
    boolean bundleInArray = getBundleInArray();
    boolean bundleInList = getBundleInList();
    boolean precedeParamsWithNames = getPrecedeParamsWithNames();
    return Objects.equals(parameterHandler, another.parameterHandler)
        && autoScanEachParam == another.getAutoScanEachParam()
        && bundleInMap == another.getBundleInMap()
        && bundleInArray == another.getBundleInArray()
        && bundleInList == another.getBundleInList()
        && precedeParamsWithNames == another.getPrecedeParamsWithNames()
        && Objects.equals(paramOrder, another.paramOrder);
  }

  /**
   * Computes a hash code from attributes: {@code parameterHandler}, {@code autoScanEachParam}, {@code bundleInMap}, {@code bundleInArray}, {@code bundleInList}, {@code precedeParamsWithNames}, {@code paramOrder}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + Objects.hashCode(parameterHandler);
    boolean autoScanEachParam = getAutoScanEachParam();
    h += (h << 5) + Booleans.hashCode(autoScanEachParam);
    boolean bundleInMap = getBundleInMap();
    h += (h << 5) + Booleans.hashCode(bundleInMap);
    boolean bundleInArray = getBundleInArray();
    h += (h << 5) + Booleans.hashCode(bundleInArray);
    boolean bundleInList = getBundleInList();
    h += (h << 5) + Booleans.hashCode(bundleInList);
    boolean precedeParamsWithNames = getPrecedeParamsWithNames();
    h += (h << 5) + Booleans.hashCode(precedeParamsWithNames);
    h += (h << 5) + Objects.hashCode(paramOrder);
    return h;
  }

  /**
   * Generates a string representation of this {@code IJavaParameterizationAux}.
   * If uninitialized, some attribute values may appear as question marks.
   * @return A string representation
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper("MutableJavaParameterizationAux")
        .add("parameterHandler", getParameterHandler())
        .add("autoScanEachParam", getAutoScanEachParam())
        .add("bundleInMap", getBundleInMap())
        .add("bundleInArray", getBundleInArray())
        .add("bundleInList", getBundleInList())
        .add("precedeParamsWithNames", getPrecedeParamsWithNames())
        .add("paramOrder", getParamOrder())
        .toString();
  }
}
