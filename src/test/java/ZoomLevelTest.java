import BFST20Project.AddressParser;
import BFST20Project.Trie;
import BFST20Project.WayType;
import BFST20Project.ZoomLevel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ZoomLevelTest {
@Test
    public void testZoomLevel(){
        ZoomLevel a = ZoomLevel.levelForWayType(WayType.BREAKWATER);
        Assertions.assertEquals(a,ZoomLevel.LEVEL_1);
        ZoomLevel b = ZoomLevel.levelForWayType(WayType.BEACH);
        Assertions.assertEquals(b,ZoomLevel.LEVEL_2);
        ZoomLevel c = ZoomLevel.levelForWayType(WayType.ASPHALT);
        Assertions.assertEquals(c,ZoomLevel.LEVEL_3);

    }

}
