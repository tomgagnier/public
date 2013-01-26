/////////////////////////////////////////////////////////////////////////////
// Name: cpp-listener.h
//
// Description: Abstract listener class for cppunit based off of JUnit
//      (http://www.junit.org) and a simliar c++ framework developed
//      by Tom Gagnier
//
///////////////////////////////////////////////////////////////////////////////

#ifndef _CPPUNIT_LISTENER_H_
#define _CPPUNIT_LISTENER_H_

#include <string>

namespace cppunit
{
  class TestNode;
  class TestResult;
  class CppUnitListener {
  public:
    virtual void onStartSuite(const TestNode * ts) const = 0;
    virtual void onStartTest(const TestNode * tc) const = 0;
    virtual void onReportResult(const TestResult& tr) const = 0;
    virtual void onEndTest(const TestNode * tc) const = 0;
    virtual void onEndSuite(const TestNode * ts) const = 0;
    virtual void onPrintln(const std::string &str) const = 0;
  };
  class DefaultListener : public CppUnitListener {
  public:
    static const DefaultListener* getInstance() {
      if (s_instance == 0) s_instance = new DefaultListener;
      return s_instance;
    }

    void onStartSuite(const TestNode *) const;
    void onStartTest(const TestNode *) const;
    void onReportResult(const TestResult& tr) const;
    void onEndTest(const TestNode *) const;
    void onEndSuite(const TestNode *) const;
    void onPrintln(const std::string &str) const;
  private:
    std::string doTab() const;
    DefaultListener();
    DefaultListener(const DefaultListener&); //broke
    DefaultListener& operator=(const DefaultListener&);//broke
    static DefaultListener* s_instance;
    enum Constants {
      tabSize = 4
    };
  private:
    mutable int m_nTabCount;
    mutable int m_nTested;
    mutable int m_nFailed;
  };
}
#endif
