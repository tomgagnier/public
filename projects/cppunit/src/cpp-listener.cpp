/////////////////////////////////////////////////////////////////////////////
// Name: cpp-listner.cpp
//
// Description: class for cppunit DefaultListener implementation based off of JUnit
//		(http://www.junit.org) and a simliar c++ framework developed
//		by Tom Gagnier
//
///////////////////////////////////////////////////////////////////////////////

#ifdef WIN32
#pragma warning(disable: 4786)
#include <windows.h>
#endif

#include "cppunit/cppunit.h"
#include <sstream>
#include <iostream>

using cppunit::DefaultListener;
using cppunit::TestNode;
using cppunit::TestResult;
using std::string;

DefaultListener* DefaultListener::s_instance = 0;

DefaultListener::DefaultListener() : m_nTabCount(0),m_nTested(0), m_nFailed(0)
{

}

string DefaultListener::doTab() const
{
    return std::string(tabSize * m_nTabCount, ' ');
}

void DefaultListener::onPrintln(const std::string &str) const
{
    std::cout << doTab() << str << std::endl;
    #ifdef WIN32
    ::OutputDebugString(doTab().c_str());
    ::OutputDebugString(str.c_str());
    ::OutputDebugString("\n");
    #endif
}

void DefaultListener::onStartSuite(const TestNode * ts) const
{
    onPrintln(ts->getName());
	++m_nTabCount;
}

void DefaultListener::onStartTest(const TestNode * tc) const
{
    onPrintln(tc->getName());
    ++m_nTabCount;
}

void DefaultListener::onReportResult(const TestResult& tr) const
{
    if(tr == bool(false))
    {
		++m_nFailed;
    }
    ++m_nTested;
	onPrintln(tr.toString());
}

void DefaultListener::onEndTest(const TestNode * tc) const
{
	--m_nTabCount;
	onPrintln(tc->getName());
}

void DefaultListener::onEndSuite(const TestNode * ts) const
{
	--m_nTabCount;
	onPrintln(ts->getName());
    std::ostringstream out;
    out << m_nFailed << " failed tests out of " << m_nTested;
    onPrintln(out.str());
}

