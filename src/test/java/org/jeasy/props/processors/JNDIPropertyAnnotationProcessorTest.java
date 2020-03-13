/*
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package org.jeasy.props.processors;

import org.jeasy.props.annotations.JNDIProperty;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;

import static org.assertj.core.api.Assertions.assertThat;

public class JNDIPropertyAnnotationProcessorTest extends AbstractAnnotationProcessorTest {

    private Context context;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        context = new InitialContext();
        context.bind("foo.property", "jndi");
    }

    @Test
    public void testJNDIPropertyInjection() {
        //given
        Bean bean = new Bean();

        //when
        propertiesInjector.injectProperties(bean);

        //then
        assertThat(bean.getJndiProperty()).isEqualTo("jndi");
    }

    @Test
    public void whenKeyIsMissing_thenShouldSilentlyIgnoreTheField() {
        //given
        BeanWithInvalidKey bean = new BeanWithInvalidKey();

        //when
        propertiesInjector.injectProperties(bean);

        //then
        assertThat(bean.getJndiProperty()).isNull();
    }

    @After
    public void tearDown() throws Exception {
        context.close();
    }

    public static class Bean {

        @JNDIProperty("foo.property")
        private String jndiProperty;

        public String getJndiProperty() { return jndiProperty; }
        public void setJndiProperty(String jndiProperty) { this.jndiProperty = jndiProperty; }
    }

    public static class BeanWithInvalidKey {

        @JNDIProperty("blah")
        private String jndiProperty;

        public String getJndiProperty() { return jndiProperty; }
        public void setJndiProperty(String jndiProperty) { this.jndiProperty = jndiProperty; }
    }
}
