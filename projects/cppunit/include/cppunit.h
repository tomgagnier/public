/////////////////////////////////////////////////////////////////////////////
// Name: cppunit.h
//
// Description: class for cppunit based off of JUnit
//		(http://www.junit.org) and a simliar c++ framework developed
//		by Tom Gagnier
//
///////////////////////////////////////////////////////////////////////////////
#ifndef _CPPUNIT_TESTBENCH_H_
#define _CPPUNIT_TESTBENCH_H_

#include "cppunit/cpp-listener.h"
#include <list>

namespace cppunit 
{
	/**
	*  converts a bool to a string ...emphasizing a false
	*/
	inline std::string toString(bool b) { return b ? "true " : "FALSE";}
	
	/**
	*	Represents a location in a source code file
	*/
    class Location
    {
    public:
		Location(std::string file, int line)
			: m_strFile(file), m_nLine(line) {}
		Location(const Location &r) : m_strFile(r.m_strFile), m_nLine(r.m_nLine) {}
		/**
		* @date  12/28/2000 3:43:01 PM
		* @return  std::string 
		*  
		* Converts Location to a formated string 
		*/
        std::string toString() const;
    private:
        std::string m_strFile;
        int         m_nLine;
    };

	/**
	*	Represents the results of a boolean test pass to the framework.
	*/
    class TestResult
	{
	public:
		TestResult(bool bResult,const std::string &strExpression,const Location &location)
            :   m_bResult(bResult),
                m_strExpression(strExpression),
                m_Location(location) {}
		/**
		* @date  12/28/2000 3:44:31 PM
		* @return  std::string 
		*  
		*  formats the TestResult data into a string
		*/
		std::string toString() const;
		bool operator==(const bool &b) const {return b==m_bResult;}
		bool operator!=(const bool &b) const {return b!=m_bResult;}
    private:
		bool        m_bResult;
		std::string m_strExpression;
		Location    m_Location;
	};

	/**
	*	Abstract base class for TestCase and TestSuite.  It is derived from CountedBase
	*	So we don't have to do memory clean up in the TestSuite.  But this does mean that we must
	*	have pointers from the free store, not from the stack.
	*/
	class TestNode : public stdplus::CountedBase
	{
		typedef std::list<const CppUnitListener *> LISTENER_LIST;
		typedef LISTENER_LIST::const_iterator LISTENER_LIST_IT;
	public:
		/**
		*  pure virtual function for running the testcase or suite
		*/
		virtual void run()=0;
		/**
		*  Adds a listener to this node.  The listeners are used to report happenings
		*/
		void addListener(const CppUnitListener *pListener) 
		{
			m_Listeners.insert(m_Listeners.end(),pListener);
		}
		/**
		*  Messages to the listeners to print the given string
		*/
		void println(const std::string &str = std::string("")) const;
		/**
		*  lazy eval of the name of the class...this is obtained through rtti info
		*/
		std::string getName() const;
	protected:
		TestNode();
		TestNode(const CppUnitListener *pListener);
		TestNode(const LISTENER_LIST &list) : m_Listeners(list) {}
		void notifyOnSuite(const TestNode * ts, bool bStart);
		void notifyOnTest(const TestNode * tc, bool bStart);
		void notifyOnResults(const TestResult & tr);
		void makeResult(bool bResult, const std::string &expression, const Location &loc);
		virtual ~TestNode(){}
	private:
		LISTENER_LIST m_Listeners;
		mutable std::string m_strName; //lazy eval
	};

	/**
	*	This is the class you will derive from to write your tests.  You will need to 
	*	write at least a void run() method.
	*/
	class TestCase : public TestNode
	{
	public:
		TestCase() : TestNode() {}
		virtual ~TestCase() {}
	};

	/**
	*	TestSuite.  This keeps a collection of TestCases and TestSuites.  Use the add runtion
	*	to add any type derived from TestNode to the run list.  
	*/
	class TestSuite : public TestNode
	{
		typedef stdplus::RefCountedObj<TestNode> TNODE;
	public:
		/**
		*  Allows you to add any class derived from TestNode to the run list
		*	These pointers should come from the free store.  And the same pointer 
		*	can safely be added more than once.  Because the list is of RefCountedObj<TestNode>
		*	Memory clean up will be handed by the RefCountedObj class
		*/
		virtual TestSuite& add(TestNode* node);
		/**
		*  Runs member of the run list in the order it was given
		*/
		virtual void run();
	private:
		typedef std::list<TNODE> TEST_NODE_LIST;
		typedef TEST_NODE_LIST::iterator TEST_NODE_LIST_IT;
		TEST_NODE_LIST m_RunList;
	};

////////////////////////////////////////////////////////////////////////////////////////
//	Macros for ease of use
#ifdef testResult
    #error testResult already defined!
#endif
#ifdef passResult
    #error passResult already defined!
#endif
#ifdef failResult
    #error failResult already defined!
#endif
#ifdef testException
    #error testException  already defined!
#endif

#define testResult(expression) makeResult((expression), #expression, cppunit::Location(__FILE__,__LINE__));
#define passResult(message)    makeResult(true,         (message),   cppunit::Location(__FILE__,__LINE__));
#define failResult(message)    makeResult(false,        (message),   cppunit::Location(__FILE__,__LINE__));
#define testExceptionResult(exception, expression) \
    try { expression; failResult("failed to throw "#exception); } \
    catch (exception &e) { passResult("expected exception caught - "#expression); println(e.what());};
} //cppunit

#endif
