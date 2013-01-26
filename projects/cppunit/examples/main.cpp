#ifdef WIN32
  #pragma warning(disable: 4786)
#endif
#include <stdexcept>

#include "cppunit/cppunit.h"

using cppunit::TestCase;
using cppunit::TestSuite;

struct Example : public TestSuite {
  struct ExampleCase : public TestCase {
    void run() {
      testResult(1==1);
      testResult(2*2==4);
      passResult("pass");
      failResult("This should fail");
      testExceptionResult(std::exception, throw std::domain_error("ROAR"));
    }
  };
  Example() {
    add(new ExampleCase());
  }
};

int main(int arc, char *argv[]) {
  Example e;
  e.run();
  return 0;
}
