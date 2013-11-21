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
