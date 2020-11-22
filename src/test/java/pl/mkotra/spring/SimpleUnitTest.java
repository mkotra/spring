package pl.mkotra.spring;

import org.junit.jupiter.api.Test;
import pl.mkotra.spring.storage.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SimpleUnitTest {

    @Test
    public void simpleUnitTest() {
        //given
        Item item = mock(Item.class);
        when(item.getId()).thenReturn("1");
        when(item.getName()).thenReturn("name");

        //when
        String id = item.getId();
        String name = item.getName();

        //then
        verify(item, times(1)).getId();
        verify(item, times(1)).getName();
        assertEquals("1", id);
        assertEquals("name", name);
        verifyNoMoreInteractions(item);
    }
}
