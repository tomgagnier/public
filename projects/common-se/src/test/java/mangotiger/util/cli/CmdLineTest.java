package mangotiger.util.cli;

import java.io.IOException;
import java.io.File;
import java.net.InetAddress;
import java.util.Arrays;

import junit.framework.TestCase;
import mangotiger.io.Files;
import mangotiger.lang.Strings;

/** Test @see CmdLine. */
@SuppressWarnings({"UnusedCatchParameter", "ClassWithoutToString"}) public final class CmdLineTest extends TestCase {
  private static final String CL_DESCRIPTION = "description";
  private static final String B1 = "-b1";
  private static final String B1_DESCRIPTION = "b1-description";
  private static final String B2 = "-b2";
  private static final String B2_DESCRIPTION = "b2-description";
  private static final boolean B2_DEFAULT = true;
  private static final String B3 = "-b3";
  private static final String B3_DESCRIPTION = "b3-description";
  private static final boolean B3_DEFAULT = false;
  private static final String I1 = "-i1";
  private static final String I1_DESCRIPTION = "i1-description";
  private static final String I2 = "-i2";
  private static final int I2_DEFAULT = 2;
  private static final String I2_DESCRIPTION = "i2-description";
  private static final String I3 = "-i3";
  private static final int I3_DEFAULT = -1;
  private static final String I3_DESCRIPTION = "i3-description";
  private static final String SA1 = "-sa1";
  private static final String SA1_DESCRIPTION = "sa1-description";
  private static final String SA2 = "-sa2";
  private static final String SA2_DESCRIPTION = "sa2-description";
  private static final String[] SA2_DEFAULT = new String[]{"1", "2", "3"};
  private static final String S1 = "-s1";
  private static final String S1_DESCRIPTION = "s1-description";
  private static final String S2 = "-s2";
  private static final String S2_DESCRIPTION = "s2-description";
  private static final String S2_DEFAULT = "s2-default";
  private CmdLine cmdLine;
  private BooleanParameter b1;
  private BooleanParameter b2;
  private BooleanParameter b3;
  private IntegerParameter i1;
  private IntegerParameter i2;
  private IntegerParameter i3;
  private StringArrayParameter sa1;
  private StringArrayParameter sa2;
  private StringParameter s1;
  private StringParameter s2;

  @Override protected void setUp() throws Exception {
    super.setUp();
    cmdLine = new CmdLine(getClass(), CL_DESCRIPTION);
    b1 = cmdLine.addBoolean(B1.substring(1), B1_DESCRIPTION);
    b2 = cmdLine.addBoolean(B2.substring(1), B2_DESCRIPTION, B2_DEFAULT);
    b3 = cmdLine.addBoolean(B3.substring(1), B3_DESCRIPTION, B3_DEFAULT);
    i1 = cmdLine.addInteger(I1.substring(1), I1_DESCRIPTION);
    i2 = cmdLine.addInteger(I2.substring(1), I2_DESCRIPTION, I2_DEFAULT);
    i3 = cmdLine.addInteger(I3.substring(1), I3_DESCRIPTION, I3_DEFAULT);
    sa1 = cmdLine.addStringArray(SA1.substring(1), SA1_DESCRIPTION);
    sa2 = cmdLine.addStringArray(SA2.substring(1), SA2_DESCRIPTION, SA2_DEFAULT);
    s1 = cmdLine.addString(S1.substring(1), S1_DESCRIPTION);
    s2 = cmdLine.addString(S2.substring(1), S2_DESCRIPTION, S2_DEFAULT);
  }

  /** Test no arguments. */
  public void testNoArguments() throws Exception {
    try {
      cmdLine.parse(Strings.EMPTY_ARRAY);
      fail();
    } catch (CliException e) {
      // expected
    }
  }

  /** Test normal arguments. */
  public void testNormal() throws Exception {
    final String[] args = new String[]{B1, I1, "1", SA1, "hello", "world", S1, "goodnight", "arg1", "arg2"};
    cmdLine.parse(args);
    assertCorrectValues();
    cmdLine.parse("@" + new File(Files.toFile("src/test/resources", getClass()), "CmdLineTest.txt"));
    assertCorrectValues();
  }

  private void assertCorrectValues() {
    assertEquals(Boolean.TRUE, b1.getValue());
    assertEquals(B2_DEFAULT, b2.value());
    assertEquals(B3_DEFAULT, b3.value());
    assertEquals(1, i1.value());
    assertEquals(I2_DEFAULT, i2.value());
    assertEquals(I3_DEFAULT, i3.value());
    assertEquals(Arrays.asList(new String[]{"hello", "world"}), Arrays.asList(sa1.value()));
    assertEquals(Arrays.asList(SA2_DEFAULT), Arrays.asList(sa2.value()));
    assertEquals(s1.value(), "goodnight");
    assertEquals(s2.value(), S2_DEFAULT);
    assertEquals(cmdLine.getArguments(), Arrays.asList(new String[]{"arg1", "arg2"}));
    assertEquals(Boolean.TRUE.toString(), b1.toString());
    assertEquals(S2_DEFAULT, s2.toString());
    final String expected = getClass().getName() + "{?=false" + ", b1=true, b2=true, b3=false" + ", i1=1, i2=2, i3=-1" +
                            ", s1=goodnight, s2=s2-default" + ", sa1=[hello, world], sa2=[1, 2, 3]}" + "[arg1, arg2]";
    assertEquals(expected, cmdLine.toString());
  }

  /** Test help. */
  public void testHelp() throws IOException {
    try {
      cmdLine.parse(new String[]{"-?"});
      fail();
    } catch (CliException e) {
      final String expect = getClass().getName() + " [-?] [-b1] [-b2] [-b3] {-i1} [-i2] [-i3] {-s1} " +
                            "[-s2] [-sa1 ...] [-sa2 ...]" + "\n\tdescription" +
                            "\n\t-?   Print usage text.  Optional.  No arguments." +
                            "\n\t-b1  b1-description.  Optional.  No arguments." +
                            "\n\t-b2  b2-description.  Optional.  No arguments." +
                            "\n\t-b3  b3-description.  Optional.  No arguments." +
                            "\n\t-i1  i1-description.  Required.  One argument." +
                            "\n\t-i2  i2-description.  Optional.  One argument." +
                            "\n\t-i3  i3-description.  Optional.  One argument." +
                            "\n\t-s1  s1-description.  Required.  One argument." +
                            "\n\t-s2  s2-description.  Optional.  One argument." +
                            "\n\t-sa1 sa1-description.  Optional.  Zero or more arguments." +
                            "\n\t-sa2 sa2-description.  Optional.  Zero or more arguments.";
      assertEquals(expect, cmdLine.getUsage());
    }
  }

  /** Test bad switch. */
  public void testBadSwitch() throws IOException {
    try {
      final String[] args = new String[]{"-bad-switch", B1, I1, "1", SA1, "hello", "world", S1, "goodnight", "arg1",
                                         "arg2"};
      cmdLine.parse(args);
      fail();
    } catch (CliException e) {
      assertTrue(e.getMessage().indexOf("Unrecognized switch name (-bad-switch).") != -1);
    }
  }

  /** Test bad file. */
  public void testBadFile() {
    try {
      cmdLine.parse(new String[]{"@bad-file"});
      fail();
    } catch (IOException expected) {
      assertTrue(expected.getMessage().indexOf("bad-file") != -1);
    }
  }

  /** Test empty array index. */
  public void testEmptyArrayIndex() throws Exception {
    final String[] args = new String[]{B1, I1, "1", SA1, S1, "goodnight", "arg1", "arg2"};
    cmdLine.parse(args);
    assertEquals(0, sa1.value().length);
  }

  /** Test missing argument. */
  public void testMissingArg() throws IOException {
    final CmdLine cl = new CmdLine(getClass(), "Mangotiger Login Server");
    cl.addInteger("localport", "Port on local machine to listen to");
    cl.addInteger("dataport", "Port to redirect new users to");
    cl.addString("datahost",
                 "Host to redirect to (machine name or dotted quad)",
                 InetAddress.getLocalHost().getHostName());
    try {
      cl.parse(new String[]{""});
      fail();
    } catch (CliException e) {
      // expected
    }
  }

  /** Simple test. */
  public void test() {
    cmdLine = new CmdLine(getClass(), "description");
    cmdLine.addString("string", "string description");
    cmdLine.addStringArray("stringArray", "stringArray description");
    cmdLine.addStringArray("stringArray2", "stringArray2 description", new String[]{"1", "2"});
  }

  /** Test illegal arguments. */
  public void testIllegalArguments() {
    try {
      new CmdLine(null, "");
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }
    try {
      new CmdLine(getClass(), null);
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
}
