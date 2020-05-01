/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License).  You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://glassfish.dev.java.net/public/CDDLv1.0.html or
 * glassfish/bootstrap/legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at glassfish/bootstrap/legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 */

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static javax.persistence.FetchType.EAGER;

/**
 * This annotation defines a single-valued association to
 * another entity that has one-to-one multiplicity. It is not
 * normally necessary to specify the associated target entity
 * explicitly since it can usually be inferred from the type
 * of the object being referenced.
 *
 * <pre>
 *    Example 1: One-to-one association that maps a foreign key column
 *
 *    On Customer class:
 *
 *    &#064;OneToOne(optional=false)
 *    &#064;JoinColumn(
 *    	name="CUSTREC_ID", unique=true, nullable=false, updatable=false)
 *    public CustomerRecord getCustomerRecord() { return customerRecord; }
 *
 *    On CustomerRecord class:
 *
 *    &#064;OneToOne(optional=false, mappedBy="customerRecord")
 *    public Customer getCustomer() { return customer; }
 *
 *    Example 2: One-to-one association that assumes both the source and target share the same primary key values.
 *
 *    On Employee class:
 *
 *    &#064;Entity
 *    public class Employee {
 *    	&#064;Id Integer id;
 *
 *    	&#064;OneToOne &#064;PrimaryKeyJoinColumn
 *    	EmployeeInfo info;
 *    	...
 *    }
 *
 *    On EmployeeInfo class:
 *
 *    &#064;Entity
 *    public class EmployeeInfo {
 *    	&#064;Id Integer id;
 *    	...
 *    }
 * </pre>
 *
 * @author game
 * @since Java Persistence 1.0
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)

public @interface OneToOne {

    /**
     * （可选）作为关联目标的实体类。
     *
     * <p>默认为存储关联的字段或属性的类型。
     */
    Class targetEntity() default void.class;

    /**
     * （可选）必须级联到关联目标的操作。
     *
     * <p> 默认情况下，没有操作级联。
     */
    CascadeType[] cascade() default {};

    /**
     * （可选）是否应延迟加载关联或必须热切获取关联.
     * {@link FetchType#EAGER EAGER}策略是对持久性提供程序运行时的要求，必须热切地获取关联的实体。
     * {@link FetchType#LAZY LAZY} 策略是对持久性提供程序运行时的提示。.
     */
    FetchType fetch() default EAGER;

    /**
     * （可选）关联是否为可选。如果设置为false，则必须始终存在非null关系.
     */
    boolean optional() default true;

    /**
     * （可选）拥有关系的字段。仅在关联的反（非所有权）侧指定此元素。
     */
    String mappedBy() default "";
}
