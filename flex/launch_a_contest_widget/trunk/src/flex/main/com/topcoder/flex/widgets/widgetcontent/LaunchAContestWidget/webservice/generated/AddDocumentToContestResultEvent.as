/**
 * AddDocumentToContestResultEvent.as
 * This file was auto-generated from WSDL
 * Any change made to this file will be overwritten when the code is re-generated.
*/
package com.topcoder.flex.widgets.widgetcontent.LaunchAContestWidget.webservice.generated
{
    import mx.utils.ObjectProxy;
    import flash.events.Event;
    import flash.utils.ByteArray;
    import mx.rpc.soap.types.*;
    /**
     * Typed event handler for the result of the operation
     */
    
    public class AddDocumentToContestResultEvent extends Event
    {
        /**
         * The event type value
         */
        public static var AddDocumentToContest_RESULT:String="AddDocumentToContest_result";
        /**
         * Constructor for the new event type
         */
        public function AddDocumentToContestResultEvent()
        {
            super(AddDocumentToContest_RESULT,false,false);
        }
        
        private var _headers:Object;
        private var _result:AddDocumentToContestResponse;
         public function get result():AddDocumentToContestResponse
        {
            return _result;
        }
        
        public function set result(value:AddDocumentToContestResponse):void
        {
            _result = value;
        }

        public function get headers():Object
	    {
            return _headers;
	    }
			
	    public function set headers(value:Object):void
	    {
            _headers = value;
	    }
    }
}