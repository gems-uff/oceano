/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.peixeespada.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Heliomar
 */
@NamedQueries({

    @NamedQuery(name="Refactoring.getByName",query="select t from Refactoring t where t.name = ? "),
    @NamedQuery(name="Refactoring.getAll",query="select t from Refactoring t order by t.name "),
    @NamedQuery(name="Refactoring.getByType",query="select t from Refactoring t where t.tipo = ? order by t.name ")

})

@Entity
@Table(name="espada_refactoring")
@SequenceGenerator(name = "espada_refactoring_seq", sequenceName = "espada_refactoring_seq")
public class Refactoring implements Serializable{

    public static final String REFACTORING_ENCAPSULE_FIELDS = "EncapsuleFields";
    public static final String REFACTORING_CLEAN_IMPORTS = "CleanImports";
    public static final String REFACTORING_PULL_UP_METHODS = "PullUpMethods";
    public static final String REFACTORING_PULL_UP_FIELDS = "PullUpFields";
    public static final String REFACTORING_PUSH_DOWN_METHODS = "PushDownMethods";
    public static final String REFACTORING_PUSH_DOWN_FIELDS = "PushDownFields";
    public static final String REFACTORING_ADD_DELEGATE_METHODS = "AddDelegateMethods";
    public static final String REFACTORING_EXTRACT_INTERFACES = "ExtractInterfaces";
    public static final String REFACTORING_USE_SUPER_TYPES = "UseSuperTypes";
    public static final String REFACTORING_CREATE_FACTORY_METHODS = "CreateFactoryMethods";
    public static final String REFACTORING_INLINE_METHODS = "InlineMethods";
    
    @Id @GeneratedValue(strategy=GenerationType.AUTO, generator="espada_refactoring_seq")
    private Long id;

    private int tipo;
    private String name;
    private String description;

    /**
     * @return the codigo
     */
    public Long getId() {
        return id;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setId(Long codigo) {
        this.id = codigo;
    }

    /**
     * @return the tipo
     */
    public int getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the nome
     */
    public String getName() {
        return name;
    }

    /**
     * @param nome the nome to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Refactoring)){
            return false;
        }
        return id.equals(((Refactoring)obj).id);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    /**
     * @return the descricao
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the descricao to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }

}
