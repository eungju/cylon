package cylon.creole;

import cylon.dom.ItemList;
import cylon.dom.ListItem;
import cylon.dom.OrderedList;
import cylon.dom.UnorderedList;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class DomTrunkTest {
    private DomTrunk dut;

    @Before
    public void beforeEach() {
        dut = new DomTrunk();
    }

    @Test
    public void countNodes() {
        assertThat(dut.count(ItemList.class), is(0));
        dut.descend(new OrderedList());
        assertThat(dut.count(ItemList.class), is(1));
        dut.descend(new ListItem());
        dut.descend(new UnorderedList());
        assertThat(dut.count(ItemList.class), is(2));
    }
}
