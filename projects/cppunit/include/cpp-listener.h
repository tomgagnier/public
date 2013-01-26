/////////////////////////////////////////////////////////////////////////////
// Name: cpp-listener.h
//
// Description: Abstract listener class for cppunit based off of JUnit
//		(http://www.junit.org) and a simliar c++ framework developed
//		by Tom Gagnier
//
///////////////////////////////////////////////////////////////////////////////

#ifndef _CPPUNIT_LISTENER_H_
#define _CPPUNIT_LISTENER_H_

#include <string>

namespace cppunit
{
	//forward defs
	class TestNode; 
	class TestResult;
	class CppUnitListener
	{
	public:
		virtual void onStartSuite(const TestNode * ts) const = 0;
		virtual void onStartTest(const TestNode * tc) const = 0;
		virtual void onReportResult(const TestResult& tr) const = 0;
		virtual void onEndTest(const TestNode * tc) const = 0;
		virtual void onEndSuite(const TestNode * ts) const = 0;
		virtual void onPrintln(const std::string &str) const = 0;
	};
	class DefaultListener : public CppUnitListener
	{
	public:
		/**
		* @date  1/10/2001 8:52:19 PM
		* @return  const DefaultListener* 
		*  
		*  singleton
		*/
		static const DefaultListener* getInstance()
		{
			if (s_instance == 0) s_instance = new DefaultListener;
			return s_instance;
		}
		/**
		* @date  1/10/2001 8:52:33 PM
		* @return  void 
		* @param  const TestNode *
		*  
		*  
		*/
		void onStartSuite(const TestNode *) const;
		/**
		* @date  1/10/2001 8:52:34 PM
		* @return  void 
		* @param  const TestNode *
		*  
		*  
		*/
        void onStartTest(const TestNode *) const;
		/**
		* @date  1/10/2001 8:52:39 PM
		* @return  void 
		* @param  const TestResult& tr
		*  
		*  
		*/
        void onReportResult(const TestResult& tr) const;
		/**
		* @date  1/10/2001 8:53:05 PM
		* @return  void 
		* @param  const TestNode *
		*  
		*  
		*/
        void onEndTest(const TestNode *) const;
		/**
		* @date  1/10/2001 8:53:15 PM
		* @return  void 
		* @param  const TestNode *
		*  
		*  
		*/
        void onEndSuite(const TestNode *) const;
		/**
		* @date  1/10/2001 8:53:20 PM
		* @return  void 
		* @param  const std::string &str
		*  
		*  
		*/
		void onPrintln(const std::string &str) const;
    private:
		std::string doTab() const;
		DefaultListener();
		DefaultListener(const DefaultListener&); //broke
		DefaultListener& operator=(const DefaultListener&);//broke
		static DefaultListener* s_instance;
		enum Constants { tabSize = 4 };
	private:
		mutable int m_nTabCount;
		mutable int m_nTested;
		mutable int m_nFailed;
	};
}
}
#endif
