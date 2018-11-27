/**
 * generated by Xtext 2.15.0
 */
package de.upb.sede.dsl.seco;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Instruction</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.upb.sede.dsl.seco.Instruction#getField <em>Field</em>}</li>
 *   <li>{@link de.upb.sede.dsl.seco.Instruction#getContext <em>Context</em>}</li>
 *   <li>{@link de.upb.sede.dsl.seco.Instruction#getMethod <em>Method</em>}</li>
 *   <li>{@link de.upb.sede.dsl.seco.Instruction#getInputs <em>Inputs</em>}</li>
 * </ul>
 *
 * @see de.upb.sede.dsl.seco.SecoPackage#getInstruction()
 * @model
 * @generated
 */
public interface Instruction extends EObject
{
  /**
   * Returns the value of the '<em><b>Field</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Field</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Field</em>' attribute.
   * @see #setField(String)
   * @see de.upb.sede.dsl.seco.SecoPackage#getInstruction_Field()
   * @model
   * @generated
   */
  String getField();

  /**
   * Sets the value of the '{@link de.upb.sede.dsl.seco.Instruction#getField <em>Field</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Field</em>' attribute.
   * @see #getField()
   * @generated
   */
  void setField(String value);

  /**
   * Returns the value of the '<em><b>Context</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Context</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Context</em>' attribute.
   * @see #setContext(String)
   * @see de.upb.sede.dsl.seco.SecoPackage#getInstruction_Context()
   * @model
   * @generated
   */
  String getContext();

  /**
   * Sets the value of the '{@link de.upb.sede.dsl.seco.Instruction#getContext <em>Context</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Context</em>' attribute.
   * @see #getContext()
   * @generated
   */
  void setContext(String value);

  /**
   * Returns the value of the '<em><b>Method</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Method</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Method</em>' attribute.
   * @see #setMethod(String)
   * @see de.upb.sede.dsl.seco.SecoPackage#getInstruction_Method()
   * @model
   * @generated
   */
  String getMethod();

  /**
   * Sets the value of the '{@link de.upb.sede.dsl.seco.Instruction#getMethod <em>Method</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Method</em>' attribute.
   * @see #getMethod()
   * @generated
   */
  void setMethod(String value);

  /**
   * Returns the value of the '<em><b>Inputs</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Inputs</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Inputs</em>' containment reference.
   * @see #setInputs(Parameters)
   * @see de.upb.sede.dsl.seco.SecoPackage#getInstruction_Inputs()
   * @model containment="true"
   * @generated
   */
  Parameters getInputs();

  /**
   * Sets the value of the '{@link de.upb.sede.dsl.seco.Instruction#getInputs <em>Inputs</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Inputs</em>' containment reference.
   * @see #getInputs()
   * @generated
   */
  void setInputs(Parameters value);

} // Instruction