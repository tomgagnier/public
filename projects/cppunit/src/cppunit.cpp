/////////////////////////////////////////////////////////////////////////////
// Name: cppunit.cpp
//
// Description: class for cppunit based off of JUnit
//		(http://www.junit.org) and a simliar c++ framework developed
//		by Tom Gagnier
//
///////////////////////////////////////////////////////////////////////////////

#ifdef WIN32
#pragma warning(disable: 4786)
#include <algorithm>
//vc 6.0 does not follow standard so we'll fix it.
//() stop preprocessor macro expansion.
namespace std 
{
	template<typename T> T (max)(const T &x, const T&y) {return std::_cpp_max(x,y);}
}
#else
#include <algorithm>
#endif

#include <sstream>
#include <typeinfo>
#include "cppunit/cppunit.h"

using namespace cppunit;
using std::string;

////////////////////////////////////////////////////////////////////////////
//	Location
string Location::toString() const
{
    std::ostringstream ss;
    ss << m_strFile << '(' << m_nLine << ')';
    return ss.str();
}

//////////////////////////////////////////////////////////////////////////////
//	TestResult
std::string TestResult::toString() const
{
	std::ostringstream ss;
    ss << cppunit::toString(m_bResult) << " " << m_strExpression << " " << m_Location.toString() << std::ends;
    return ss.str();
}

//////////////////////////////////////////////////////////////////////////
//	testNode

TestNode::TestNode()
{
	addListener(DefaultListener::getInstance());
}
TestNode::TestNode(const CppUnitListener *pListener)
{
	addListener(pListener);
}
string TestNode::getName() const
{
    if (m_strName.empty())
    {
        m_strName = typeid(*this).name();
    }
    return m_strName;
}
void TestNode::notifyOnSuite(const TestNode * ts, bool bStart)
{
	LISTENER_LIST_IT it(m_Listeners.begin());
	for(;it!=m_Listeners.end();++it)
	{
		if(bStart)
		{
			(*it)->onStartSuite(ts);
		}
		else
		{
			(*it)->onEndSuite(ts);
		}
	}
}
void TestNode::notifyOnTest(const TestNode * tc, bool bStart)
{
	LISTENER_LIST_IT it(m_Listeners.begin());
	for(;it!=m_Listeners.end();++it)
	{
		if(bStart)
		{
			(*it)->onStartTest(tc);
		}
		else
		{
			(*it)->onEndTest(tc);
		}
	}
}
void TestNode::notifyOnResults(const TestResult & tr)
{
	LISTENER_LIST_IT it(m_Listeners.begin());
	for(;it!=m_Listeners.end();++it)
	{
		(*it)->onReportResult(tr);
	}
}
void TestNode::println(const string &str) const
{
	LISTENER_LIST_IT it(m_Listeners.begin());
	for(;it!=m_Listeners.end();++it)
	{
		(*it)->onPrintln(str);
	}
}
void TestNode::makeResult(bool bResult, const string &expression, const Location &loc)
{
	TestResult ts(bResult,expression,loc);
	notifyOnResults(ts);
}

////////////////////////////////////////////////////////////////////////////
// Suite
TestSuite& TestSuite::add(TestNode* testNode)
{
    m_RunList.insert(m_RunList.end(),TNODE(testNode));
    return *this;
}

void TestSuite::run()
{
	notifyOnSuite(this,true);
    TEST_NODE_LIST_IT it(m_RunList.begin());
	TestNode *pTestNode;
    for (;it!= m_RunList.end();++it)
    {
		pTestNode = (TestNode *)*it;
		notifyOnTest(pTestNode,true);
        try
        {
            pTestNode->run();
        }
        catch (std::exception& e)
        {
            makeResult(false, e.what(), Location(__FILE__, __LINE__));
        }
        catch (...)
        {
            makeResult(false,"unknown exception",Location(__FILE__, __LINE__));
        }
        notifyOnTest(pTestNode,false);
    }
	notifyOnSuite(this,false);
}
