package com.o19s.es.ltr.ranker.parser.json;

import com.o19s.es.ltr.ranker.parser.json.tree.ParsedSplit;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by doug on 5/26/17.
 */
public class SplitParserTest extends JsonModelParsingTest {

    public void testBasicSplit() throws IOException {
        String split = "{" +
                " \"feature\": \"foo\"," +
                " \"threshold\": 0.5,  " +
                " \"lhs\":    " +
                "    {\"output\": 5.0},"+
                " \"rhs\": " +
                "    {\"output\": 1.0}"+
                "}";


        ParsedSplit ps = ParsedSplit.parse(makeXContent(split));
        assert(ps.feature().equals("foo"));
        assert(ps.threshold() == 0.5);
        assert(ps.lhs() != null);
        assert(ps.rhs() != null);
        assert(ps.lhs().output() == 5.0);
        assert(ps.rhs().output() == 1.0);

    }

    public void testNestedSplit() throws IOException {
        String split = "{" +
                " \"feature\": \"foo\"," +
                " \"threshold\": 0.5,  " +
                " \"lhs\": {\"split\":  " +
                "           {\"feature\": \"bar\"," +
                "            \"threshold\": 12.0," +
                "            \"lhs\": " +
                "               {\"output\": 100.0}," +
                "            \"rhs\": " +
                "               {\"output\": 500.0}" +
                "       " +
                "    }},"+
                "    \"rhs\": {\"output\": 1.0}"+
                "}";


        ParsedSplit ps = ParsedSplit.parse(makeXContent(split));
        assert(ps.feature().equals("foo"));
        assert(ps.threshold() == 0.5);
        assert(ps.lhs() != null);
        assert(ps.rhs() != null);
        assert(ps.lhs().threshold() == 12.0);
        assert(ps.lhs().lhs() != null);
        assert(ps.lhs().rhs() != null);
        assert(ps.lhs().lhs().output() == 100.0);
        assert(ps.lhs().rhs().output() == 500.0);
        assert(ps.rhs().output() == 1.0);

    }

    public void testAutoGeneratedSplit() throws IOException {
        String split = "{\n" +
                "  \"feature\": \" 1 \",\n" +
                "  \"rhs\": {\n" +
                "      \"output\": -0.5782588720321655\n" +
                "  },\n" +
                "  \"lhs\": {\n" +
                "      \"split\": {\n" +
                "          \"feature\": \" 6 \",\n" +
                "          \"rhs\": {\n" +
                "              \"output\": \" -1.3386174440383911 \"\n" +
                "          },\n" +
                "          \"lhs\": {\n" +
                "              \"split\": {\n" +
                "                  \"feature\": \" 3 \",\n" +
                "                  \"rhs\": {\n" +
                "                      \"output\": \" 1.1761366128921509 \"\n" +
                "                  },\n" +
                "                  \"lhs\": {\n" +
                "                      \"split\": {\n" +
                "                          \"feature\": \" 6 \",\n" +
                "                          \"rhs\": {\n" +
                "                              \"output\": \" 0.037376404 \"\n" +
                "                          },\n" +
                "                          \"lhs\": {\n" +
                "                              \"output\": \" -0.04588231444358826 \"\n" +
                "                          },\n" +
                "                          \"threshold\": \" -1.0 \"\n" +
                "                      }\n" +
                "                  },\n" +
                "                  \"threshold\": \" 19.58304 \"\n" +
                "              }\n" +
                "          },\n" +
                "          \"threshold\": \" 12.945955 \"\n" +
                "      }\n" +
                "  },\n" +
                "  \"threshold\": \" 0.0 \"}\n";


        ParsedSplit ps = ParsedSplit.parse(makeXContent(split));
        assertEquals(ps.feature(), " 1 ");
        assertEquals(ps.rhs().output(), -0.57825887, 0.01);
        assertEquals(ps.lhs().feature(), " 6 ");
        assertEquals(ps.lhs().rhs().output(), -1.33861744, 0.01);
        assertEquals(ps.lhs().lhs().lhs().rhs().output(), 0.03737, 0.01);
        assertEquals(ps.lhs().lhs().lhs().lhs().output(), -0.04588231, 0.01);
        assertEquals(ps.lhs().lhs().lhs().threshold(), -1.0, 0.01);



    }
}