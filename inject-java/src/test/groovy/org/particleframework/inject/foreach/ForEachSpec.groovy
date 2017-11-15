/*
 * Copyright 2017 original authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package org.particleframework.inject.foreach

import org.particleframework.context.ApplicationContext
import org.particleframework.context.DefaultApplicationContext
import org.particleframework.context.env.MapPropertySource
import org.particleframework.inject.qualifiers.Qualifiers
import spock.lang.Ignore
import spock.lang.Specification
/**
 * @author Graeme Rocher
 * @since 1.0
 */
class ForEachSpec extends Specification {

    void "test configuration properties binding"() {
        given:
        ApplicationContext applicationContext = new DefaultApplicationContext("test")
        applicationContext.environment.addPropertySource(MapPropertySource.of(
                'foo.bar.one.port':'8080',
                'foo.bar.one.anotherPort':'9090',
                'foo.bar.one.intList':"1,2,3",
                'foo.bar.one.stringList':"1,2",
                'foo.bar.one.inner.enabled':'true',
                'foo.bar.one.flags.one':'1',
                'foo.bar.one.flags.two':'2',
                'foo.bar.one.urlList':"http://test.com, http://test2.com",
                'foo.bar.one.urlList2':["http://test.com", "http://test2.com"],
                'foo.bar.one.url':'http://test.com',

                'foo.bar.two.port':'8888',
                'foo.bar.two.anotherPort':'9999',
                'foo.bar.two.intList':"4,5,6",
                'foo.bar.two.stringList':"1,2",
                'foo.bar.two.inner.enabled':'false',
                'foo.bar.two.flags.two':'2',
                'foo.bar.two.flags.three':'3',
                'foo.bar.two.urlList':"http://test.com, http://test2.com",
                'foo.bar.two.urlList2':["http://test.com", "http://test2.com"],
                'foo.bar.two.url':'http://test.com'


        ))

        applicationContext.start()

        MyConfiguration bean = applicationContext.getBean(MyConfiguration)
        MyConfiguration bean2 = applicationContext.getBean(MyConfiguration, Qualifiers.byName("two"))

        expect:
        bean.port == 8080
        bean.anotherPort == 9090
        bean.intList == [1,2,3]
        bean.urlList == [new URL('http://test.com'),new URL('http://test2.com')]
        bean.flags == [one:1, two:2]
        bean.urlList2 == [new URL('http://test.com'),new URL('http://test2.com')]
        bean.stringList == ["1", "2"]
        bean.emptyList == null
        bean.url.get() == new URL('http://test.com')
        !bean.anotherUrl.isPresent()
        bean.defaultPort == 9999
        bean.defaultValue == 9999
        bean.primitiveDefaultValue == 9999
        bean.inner.enabled == 'true'

        bean2.port == 8888
        bean2.anotherPort == 9999
        bean2.intList == [4,5,6]
        bean2.urlList == [new URL('http://test.com'),new URL('http://test2.com')]
        bean2.flags == [two:2, three:3]
        bean2.urlList2 == [new URL('http://test.com'),new URL('http://test2.com')]
        bean2.stringList == ["1", "2"]
        bean2.emptyList == null
        bean2.url.get() == new URL('http://test.com')
        !bean2.anotherUrl.isPresent()
        bean2.defaultPort == 9999
        bean2.defaultValue == 9999
        bean2.primitiveDefaultValue == 9999
        bean2.inner.enabled == 'false'


        cleanup:
        applicationContext.close()
    }

    void "test configuration properties binding by bean type"() {
        given:
        ApplicationContext applicationContext = new DefaultApplicationContext("test")
        applicationContext.environment.addPropertySource(new MapPropertySource(
                'foo.bar.one.port':'8080',
                'foo.bar.one.anotherPort':'9090',
                'foo.bar.one.intList':"1,2,3",
                'foo.bar.one.stringList':"1,2",
                'foo.bar.one.inner.enabled':'true',
                'foo.bar.one.flags.one':'1',
                'foo.bar.one.flags.two':'2',
                'foo.bar.one.urlList':"http://test.com, http://test2.com",
                'foo.bar.one.urlList2':["http://test.com", "http://test2.com"],
                'foo.bar.one.url':'http://test.com',

                'foo.bar.two.port':'8888',
                'foo.bar.two.anotherPort':'9999',
                'foo.bar.two.intList':"4,5,6",
                'foo.bar.two.stringList':"1,2",
                'foo.bar.two.inner.enabled':'false',
                'foo.bar.two.flags.two':'2',
                'foo.bar.two.flags.three':'3',
                'foo.bar.two.urlList':"http://test.com, http://test2.com",
                'foo.bar.two.urlList2':["http://test.com", "http://test2.com"],
                'foo.bar.two.url':'http://test.com'


        ))

        applicationContext.start()

        MyBean bean = applicationContext.getBean(MyBean)
        MyBean bean2 = applicationContext.getBean(MyBean, Qualifiers.byName("two"))

        expect:
        bean != bean2
        bean.configuration.port == 8080
        bean.configuration.anotherPort == 9090
        bean.configuration.intList == [1,2,3]
        bean.configuration.urlList == [new URL('http://test.com'),new URL('http://test2.com')]
        bean.configuration.flags == [one:1, two:2]
        bean.configuration.urlList2 == [new URL('http://test.com'),new URL('http://test2.com')]
        bean.configuration.stringList == ["1", "2"]
        bean.configuration.emptyList == null
        bean.configuration.url.get() == new URL('http://test.com')
        !bean.configuration.anotherUrl.isPresent()
        bean.configuration.defaultPort == 9999
        bean.configuration.defaultValue == 9999
        bean.configuration.primitiveDefaultValue == 9999
        bean.configuration.inner.enabled == 'true'

        bean2.configuration.port == 8888
        bean2.configuration.anotherPort == 9999
        bean2.configuration.intList == [4,5,6]
        bean2.configuration.urlList == [new URL('http://test.com'),new URL('http://test2.com')]
        bean2.configuration.flags == [two:2, three:3]
        bean2.configuration.urlList2 == [new URL('http://test.com'),new URL('http://test2.com')]
        bean2.configuration.stringList == ["1", "2"]
        bean2.configuration.emptyList == null
        bean2.configuration.url.get() == new URL('http://test.com')
        !bean2.configuration.anotherUrl.isPresent()
        bean2.configuration.defaultPort == 9999
        bean2.configuration.defaultValue == 9999
        bean2.configuration.primitiveDefaultValue == 9999
        bean2.configuration.inner.enabled == 'false'


        cleanup:
        applicationContext.close()
    }

    void "test configuration properties binding by a non bean type"() {
        given:
        ApplicationContext applicationContext = new DefaultApplicationContext("test")
        applicationContext.environment.addPropertySource(new MapPropertySource(
                'foo.bar.one.port':'8080',
                'foo.bar.two.port':'8888',
        ))

        applicationContext.start()

        NonBeanClass bean = applicationContext.getBean(NonBeanClass)
        NonBeanClass bean2 = applicationContext.getBean(NonBeanClass, Qualifiers.byName("two"))

        expect:
        bean != bean2
        bean.port == 8080
        bean2.port == 8888

        cleanup:
        applicationContext.close()
    }

    void "test configuration properties binding by bean type with primary"() {
        given:
        ApplicationContext applicationContext = new DefaultApplicationContext("test")
        applicationContext.environment.addPropertySource(new MapPropertySource(
                'foo.bar.one.port':'8080',
                'foo.bar.one.anotherPort':'9090',
                'foo.bar.one.intList':"1,2,3",
                'foo.bar.one.stringList':"1,2",
                'foo.bar.one.inner.enabled':'true',
                'foo.bar.one.flags.one':'1',
                'foo.bar.one.flags.two':'2',
                'foo.bar.one.urlList':"http://test.com, http://test2.com",
                'foo.bar.one.urlList2':["http://test.com", "http://test2.com"],
                'foo.bar.one.url':'http://test.com',

                'foo.bar.two.port':'8888',
                'foo.bar.two.anotherPort':'9999',
                'foo.bar.two.intList':"4,5,6",
                'foo.bar.two.stringList':"1,2",
                'foo.bar.two.inner.enabled':'false',
                'foo.bar.two.flags.two':'2',
                'foo.bar.two.flags.three':'3',
                'foo.bar.two.urlList':"http://test.com, http://test2.com",
                'foo.bar.two.urlList2':["http://test.com", "http://test2.com"],
                'foo.bar.two.url':'http://test.com'


        ))

        applicationContext.start()

        MyBeanWithPrimary bean2 = applicationContext.getBean(MyBeanWithPrimary)
        MyBeanWithPrimary bean = applicationContext.getBean(MyBeanWithPrimary, Qualifiers.byName("one"))

        expect:
        bean.configuration.port == 8080
        bean.configuration.anotherPort == 9090
        bean.configuration.intList == [1,2,3]
        bean.configuration.urlList == [new URL('http://test.com'),new URL('http://test2.com')]
        bean.configuration.flags == [one:1, two:2]
        bean.configuration.urlList2 == [new URL('http://test.com'),new URL('http://test2.com')]
        bean.configuration.stringList == ["1", "2"]
        bean.configuration.emptyList == null
        bean.configuration.url.get() == new URL('http://test.com')
        !bean.configuration.anotherUrl.isPresent()
        bean.configuration.defaultPort == 9999
        bean.configuration.defaultValue == 9999
        bean.configuration.primitiveDefaultValue == 9999
        bean.configuration.inner.enabled == 'true'

        bean2.configuration.port == 8888
        bean2.configuration.anotherPort == 9999
        bean2.configuration.intList == [4,5,6]
        bean2.configuration.urlList == [new URL('http://test.com'),new URL('http://test2.com')]
        bean2.configuration.flags == [two:2, three:3]
        bean2.configuration.urlList2 == [new URL('http://test.com'),new URL('http://test2.com')]
        bean2.configuration.stringList == ["1", "2"]
        bean2.configuration.emptyList == null
        bean2.configuration.url.get() == new URL('http://test.com')
        !bean2.configuration.anotherUrl.isPresent()
        bean2.configuration.defaultPort == 9999
        bean2.configuration.defaultValue == 9999
        bean2.configuration.primitiveDefaultValue == 9999
        bean2.configuration.inner.enabled == 'false'


        cleanup:
        applicationContext.close()
    }

    void "test configuration properties binding by non bean type with primary"() {
        given:
        ApplicationContext applicationContext = new DefaultApplicationContext("test")
        applicationContext.environment.addPropertySource(new MapPropertySource(
                'foo.bar.one.port':'8080',
                'foo.bar.two.port':'8888',
        ))

        applicationContext.start()

        NonBeanClassWithPrimary bean2 = applicationContext.getBean(NonBeanClassWithPrimary)
        NonBeanClassWithPrimary bean = applicationContext.getBean(NonBeanClassWithPrimary, Qualifiers.byName("one"))

        expect:
        bean.port == 8080
        bean2.port == 8888

        cleanup:
        applicationContext.close()
    }

    void "test resolve for empty configuration"() {
        given:
        ApplicationContext applicationContext = new DefaultApplicationContext("test")


        applicationContext.start()

        expect:
        !applicationContext.containsBean(MyBean)
        !applicationContext.containsBean(MyConfiguration)
        applicationContext.getBeansOfType(MyBean).size() == 0
        applicationContext.getBeansOfType(MyConfiguration).size() == 0
        applicationContext.streamOfType(MyBean).count() == 0
        applicationContext.streamOfType(MyConfiguration).count() == 0

        cleanup:
        applicationContext.close()
    }
}
