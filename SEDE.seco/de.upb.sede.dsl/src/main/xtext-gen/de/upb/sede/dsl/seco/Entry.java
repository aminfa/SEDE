/**
 * generated by Xtext 2.15.0
 */
package de.upb.sede.dsl.seco;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.upb.sede.dsl.seco.Entry#getInstruction <em>Instruction</em>}</li>
 * </ul>
 *
 * @see de.upb.sede.dsl.seco.SecoPackage#getEntry()
 * @model
 * @generated
 */
public interface Entry extends EObject
{
  /**
   * Returns the value of the '<em><b>Instruction</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Instruction</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Instruction</em>' containment reference.
   * @see #setInstruction(Instruction)
   * @see de.upb.sede.dsl.seco.SecoPackage#getEntry_Instruction()
   * @model containment="true"
   * @generated
   */
  Instruction getInstruction();

  /**
   * Sets the value of the '{@link de.upb.sede.dsl.seco.Entry#getInstruction <em>Instruction</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Instruction</em>' containment reference.
   * @see #getInstruction()
   * @generated
   */
  void setInstruction(Instruction value);

} // Entry