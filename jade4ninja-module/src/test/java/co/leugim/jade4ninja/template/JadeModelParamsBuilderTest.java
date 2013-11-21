/**
 * Copyright (C) 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.leugim.jade4ninja.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import ninja.Context;
import ninja.Result;
import ninja.i18n.Lang;
import ninja.i18n.Messages;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.google.common.base.Optional;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class JadeModelParamsBuilderTest {

    @InjectMocks
    private JadeModelParamsBuilder builder;

    @Mock
    private Context context;
    @Mock
    private Result result;
    @Mock
    private Messages messages;
    @Mock
    private Lang lang;

    @Test(expected = IllegalArgumentException.class)
    public void testBuildError() {
        new JadeModelParamsBuilder().build();
    }
    
    @Test
    public void shouldGetAHashMapFromNull(){
        assertTrue(builder.getRenderableResult() instanceof Map);
    }
    
    @Test
    public void shouldGetAHashMapFromAnother(){
        Map<String, Object> map = new HashMap<>();
        when(result.getRenderable()).thenReturn(map);
        assertEquals(map, builder.getRenderableResult());
    }

    @Test
    public void shouldGetAHashMapFromPojo(){
        class TestPojo{};
        TestPojo object = new TestPojo();
        when(result.getRenderable()).thenReturn(object);
        Map<String,Object> res = builder.getRenderableResult();
        assertNotNull(res);
        assertNotNull(res.get("testPojo"));
        assertEquals(object,res.get("testPojo"));
    }

    @Test
    public void shouldHaveFlash() {
        when(lang.getLanguage(context, Optional.of(result))).thenReturn(
                Optional.of(""));
        Map<String, Object> result = builder.build();
        assertTrue(result.containsKey("flash"));
    }
}
