package in.cubestack.navi;

import in.cubestack.navi.search.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CrawlerServiceTest.class,
        NaviSearchEngineTest.class,
        SearchIndexTest.class,
        SearchServiceTest.class,
        InputParserTest.class
}
)
public class AllTests {
}
