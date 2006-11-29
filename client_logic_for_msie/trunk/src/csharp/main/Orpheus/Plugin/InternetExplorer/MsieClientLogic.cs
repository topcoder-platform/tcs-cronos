/// CSharpClass File "MsieClientLogic.cs" generated by Poseidon for UML.
/// Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
/// Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.

 namespace Orpheus_Client_Logic_for_MSIE {
   namespace Orpheus {
     namespace Plugin {
       namespace InternetExplorer {

         ///  <summary>
         ///  <!--StartFragment-->This class implements the client-side logic for interacting with the web application. This client logic will be incorporated into an Internet Explorer extension that enables Internet Explorer to be used as a client.
         ///  <p>This class is decoupled from the Internet Explorer extension mechanism, to give clients a great level of flexibility when incorporating this logic into an Internet Explorer extension.</p>
         ///  <p class="MsoNormal">It is responsible with the creation of the logic objects through the configuration file and using the Object Factory component.</p>
         ///  <p class="MsoNormal">It hooks to web browser events in order to invoke component event handlers when a new page is displayed in the browser. It also starts a timer and invokes event handlers on regular time intervals.</p>
         ///  <p class="MsoNormal">This class can be used as a singleton, as well. The reason for using this class as a singleton is to have the same instance of this class for multiple opened browser windows. The way this works is as follows: when Internet Explorer is started it looks in the registry for an extension object and creates it using its GUID. In this case the extension will be some derived class of <em style="mso-bidi-font-style:normal">ToolBand</em> class. So every web browser window, regardless how it was opened, will have a different instance of a derived <em style="mso-bidi-font-style:normal">ToolBand</em> class. In order for web browser windows, opened by the main web browser like for example using <em style="mso-bidi-font-style:normal">window.open,</em> to have a reference to the same <em style="mso-bidi-font-style:normal">MsieClientLogic</em> object they must use it as a singleton.</p>
         ///  <p class="MsoNormal">One more requirement for the clients is to invoke the <em style="mso-bidi-font-style:normal">CustomizeWebBrowser</em> method whenever a new window is opened and a new extension is created, passing the newly obtain reference of the web browser, in order to provide the browser with the same customization like scripting object or custom context menu.</p>
         ///  <strong>Thread safety:</strong><!--EndFragment--> This class is thread safe. Thread safety is achived by locking on this in the BloomFilter setter and getter.         ///  </summary>
         public class MsieClientLogic {

           /// Attributes

           /// Attribute DefaultConfigurationNamepsace
           /// <summary>
           /// <p>Represents the default configuration namespace.</p>
           /// <p></p>           /// </summary>
           public string DefaultConfigurationNamepsace = "Orpheus.Plugin.InternetExplorer";

           /// Attribute DefaultObjectFactoryNamespace
           /// <summary>
           /// <p>Represents the default Object Factory namespace.</p>           /// </summary>
           public string DefaultObjectFactoryNamespace = "TopCoder.Util.ObjectFactory";

           /// Attribute webBrowser
           /// <summary>
           /// <p>Represents the web browser. It is passed in the constructor. The web reference is usually obtained in the WebBrowserSite.SetSite method and should be passed to this class. Note that popup browser windows opened form the main window browser will have a reference to this same class instance, while the webBrowser member will still point to the main browser, when this class is used as a singleton.</p>
           /// <p>Once set does not change. Can not be null.</p>           /// </summary>
           private readonly Orpheus.Plugin.InternetExplorer.Interop.WebBrowserClass webBrowser = <<constructor>>;

           /// Attribute bloomFilter
           /// <summary>
           /// <p>Represents the Bloom Filter instance. Set in the constructor either passed as a parameter or created using a configured key and the Object Factory. Used by event handlers. Returnd and set in its associated property.</p>
           /// <p>Can not be null.</p>
           /// <p></p>           /// </summary>
           private TopCoder.Util.BloomFilter.BloomFilter bloomFilter;

           /// Attribute updatesPollingTimer
           /// <summary>
           /// <p>Represents the timer preset to a configured interval. Used to fire the polling update event.</p>
           /// <p>Instantiated in the constructor and not changed afterwards. Can not be null. Returned in its associated property.</p>           /// </summary>
           private readonly System.Windows.Forms.Timer updatesPollingTimer = <<constructor>>;

           /// Attribute persistence
           /// <summary>
           /// <p>Represents the IPersistence instance. Set in the constructor either passed as a parameter or created using a configured key and the Object Factory. Used by event handlers. Returned in its associated property.</p>
           /// <p>Can not be null.</p>
           /// <p></p>           /// </summary>
           private readonly Orpheus.Plugin.InternetExplorer.IPersistence persistence = <<constructor>>;

           /// Attribute webBrowserWindowNavigator
           /// <summary>
           /// <p>Represents the IWebBrowserWindowNavigator instance. Set in the constructor either passed as a parameter or created using a configured key and the Object Factory. Used by event handlers. Returned in its associated property.</p>
           /// <p>Can not be null.</p>
           /// <p></p>           /// </summary>
           private readonly Orpheus.Plugin.InternetExplorer.IWebBrowserWindowNavigator webBrowserWindowNavigator = <<constructor>>;

           /// Attribute eventsManager
           /// <summary>
           /// <p>Represents the IExtensionEventsManager instance. Set in the constructor either passed as a parameter or created using a configured key and the Object Factory. Returned in its associated property.</p>
           /// <p>Can not be null.</p>
           /// <p></p>           /// </summary>
           private readonly Orpheus.Plugin.InternetExplorer.IExtensionEventsManager eventsManager = <<constructor>>;

           /// Attribute browserCustomization
           /// <summary>
           /// <p>Represents the IDocHostUIHandler instance. Set in the constructor either passed as a parameter or created using a configured key and the Object Factory. Used in the CustomizeWebBrowser method. Returned in its associated property.</p>
           /// <p>Can not be null.</p>
           /// <p></p>           /// </summary>
           private readonly Orpheus.Plugin.InternetExplorer.Interop.IDocHostUIHandler browserCustomization = <<constructor>>;

           /// Attribute scriptingObject
           /// <summary>
           /// <p>Represents the the scripting object. Set in the constructor either passed as a parameter or created using a configured key and the Object Factory. Used in the CustomizeWebBrowser method. </p>
           /// <p>Returned in its associated property.</p>
           /// <p>Can not be null.</p>
           /// <p></p>           /// </summary>
           private readonly object scriptingObject = <<constructor>>;

           /// Attribute WebBrowser
           /// <summary>
           /// <p>Returns the web browser.</p>           /// </summary>
           public Orpheus.Plugin.InternetExplorer.Interop.WebBrowserClass WebBrowser;

           /// Attribute BloomFilter
           /// <summary>
           /// <p>Returns or sets the bloom filter.</p>
           /// <p>Locks on this in both the getter and setter.</p>           /// </summary>
           public TopCoder.Util.BloomFilter.BloomFilter BloomFilter;

           /// Attribute UpdatesPollingTimer
           /// <summary>
           /// <p>Returns the update polling timer.</p>           /// </summary>
           public System.Windows.Forms.Timer UpdatesPollingTimer;

           /// Attribute Persistence
           /// <summary>
           /// <p>Returns the IPersistence used.</p>           /// </summary>
           public Orpheus.Plugin.InternetExplorer.IPersistence Persistence;

           /// Attribute WebBrowserWindowNavigator
           /// <summary>
           /// <p>Returns the IWebBrowserNavigator used.</p>           /// </summary>
           public Orpheus.Plugin.InternetExplorer.IWebBrowserWindowNavigator WebBrowserWindowNavigator;

           /// Attribute EventsManager
           /// <summary>
           /// <p>Returns the IExtensionEventsManager used.</p>           /// </summary>
           public Orpheus.Plugin.InternetExplorer.IExtensionEventsManager EventsManager;

           /// Attribute BrowserCustomization
           /// <summary>
           /// <p>Returns the IDocHostUIHandler implementation used.</p>           /// </summary>
           public Orpheus.Plugin.InternetExplorer.Interop.IDocHostUIHandler BrowserCustomization;

           /// Attribute ScriptingObject
           /// <summary>
           /// <p>Returns the scripting object.</p>           /// </summary>
           public object ScriptingObject;

           /// Attribute instance
           /// <summary>
           /// <p>Represents the singleton instance of this class. Set in the GetInstance method with a new instance of this class and returned afterwards by the GetInstance method.</p>
           /// <p>Can be null until set in GetInstance method. Does not change once set.</p>           /// </summary>
           private Orpheus.Plugin.InternetExplorer.MsieClientLogic instance;

           /// Attributes - AssociationEnd

           /// Association End bloomFilter
           private TopCoder.Util.BloomFilter.BloomFilter bloomFilter;

           /// Association End persistence
           private IPersistence persistence;

           /// Association End updatesPollingTimer
           private System.Windows.Forms.Timer updatesPollingTimer;

           /// Association End webBrowserWindowNavigator
           private IWebBrowserWindowNavigator webBrowserWindowNavigator;

           /// Association End webBrowser
           private Orpheus.Plugin.InternetExplorer.Interop.WebBrowserClass webBrowser;

           /// Association End eventsManager
           private IExtensionEventsManager eventsManager;

           /// Association End browserCustomization
           private Orpheus.Plugin.InternetExplorer.Interop.IDocHostUIHandler browserCustomization;

           /// Association End scriptingObject
           private ScriptingObject scriptingObject;

           /// Operations

           /// Constructor MsieClientLogic
           /// <summary>
           /// <p>Constructor.</p>
           /// <p>This constructor sets the given web browser to the class field and creates using the object factory all the required objects. It then hooks to the DocumentCompleted event of the browser and the Timer Tick event.</p>
           /// <p><strong>Implemetation details:</strong></p>
           /// <ol>
           /// <li>Sets the class field to the parameter value.</li>
           /// <li>Reads the &quot;bloom_filter&quot; configuration property and passes the value to the Object Factory to create the required instance.</li>
           /// <li>Reads the &quot;window_navigator&quot; configuration property and passes the value&nbsp; to the Object Factory to create the required instance.</li>
           /// <li>Reads the &quot;persistence&quot; configuration property and passes the value to the Object Factory to create the required instance.<!--EndFragment--></li>
           /// <li>Reads the &quot;extension_events_manager&quot; configuration property and passes the value to the Object Factory to create the required instance.<!--EndFragment--></li>
           /// <li>Reads the &quot;doc_host_ui_handler&quot; configuration property and passes the value and a reference to &quot;this&quot; to the Object Factory to create the required instance.</li>
           /// <li>Reads the &quot;scripting_object&quot; configuration property and passes the value and a reference to &quot;this&quot; to the Object Factory to create the required instance.</li>
           /// <li><!--StartFragment-->Attaches the OnDocumentCompleted event handler to the webBrowser.</li>
           /// <li>Instantiates the Timer, sets the interval to the configured &quot;poll_interval&quot; value and attaches the OnUpdatesPolling handler method to the Tick event.</li>
           /// </ol>           /// </summary>
           /// <exception>ConfigurationException to signal problems with the configuration file like missing properties or if it can not create the objects using the Object Factory.</exception>
           /// <exception>ArgumentNullException if parameter is null.</exception>
           /// <param name='webBrowser'>The web browser reference obtained in the WebBrowserSite.SetSite method.</param>
           public MsieClientLogic(Orpheus.Plugin.InternetExplorer.Interop.WebBrowserClass webBrowser) {
           }

           /// Constructor MsieClientLogic
           /// <summary>
           /// <p>Constructor.</p>
           /// <p>This constructor sets the given web browser to the class field and creates using the object factory all the required objects. It then hooks to the DocumentCompleted event of the browser and the Timer Tick event.</p>
           /// <p>Uses the given custom configuration namespaces to get the ConfigManger and the ObjectFactory.</p>
           /// <p><strong>Implemetation details:</strong></p>
           /// <ol>
           /// <li>Sets the class field to the parameter value.</li>
           /// <li>Reads the &quot;bloom_filter&quot; configuration property and passes the value to the Object Factory to create the required instance.</li>
           /// <li>Reads the &quot;window_navigator&quot; configuration property and passes the value to the Object Factory to create the required instance.</li>
           /// <li>Reads the &quot;persistence&quot; configuration property and passes the value to the Object Factory to create the required instance.<!--EndFragment--></li>
           /// <li>Reads the &quot;extension_events_manager&quot; configuration property and passes the value to the Object Factory to create the required instance.<!--EndFragment--></li>
           /// <li>Reads the &quot;doc_host_ui_handler&quot; configuration property and passes the value and a reference to &quot;this&quot; to the Object Factory to create the required instance.</li>
           /// <li>Reads the &quot;scripting_object&quot; configuration property and passes the value and a reference to &quot;this&quot; to the Object Factory to create the required instance.</li>
           /// <li><!--StartFragment-->Attaches the OnDocumentCompleted event handler to the webBrowser.</li>
           /// <li>Instantiates the Timer, sets the interval to the configured &quot;poll_interval&quot; value and attaches the OnUpdatesPolling handler method to the Tick event.</li>
           /// </ol>
           /// <p></p>
           /// <p></p>           /// </summary>
           /// <exception>ConfigurationException to signal problems with the configuration file like missing properties or if it can not create the objects using the Object Factory.</exception>
           /// <exception>ArgumentNullException if any parameter is null.</exception>
           /// <exception>ArgumentException if any parameter is empty string.</exception>
           /// <param name='webBrowser'>The web browser reference obtained in the WebBrowserSite.SetSite method.</param>
           /// <param name='configurationNamsepace'>Custom configuration namespace.</param>
           /// <param name='objectFactoryNamespace'>Custom object factory configuration namespace.</param>
           public MsieClientLogic(Orpheus.Plugin.InternetExplorer.Interop.WebBrowserClass webBrowser, string configurationNamsepace, string objectFactoryNamespace) {
           }

           /// Constructor MsieClientLogic
           /// <summary>
           /// <p>Constructor.</p>
           /// <p>This constructor sets the fields to the given parameters.It then hooks to the DocumentCompleted event of the browser and the Timer Tick event.</p>
           /// <p><strong>Implemetation details:</strong></p>
           /// <ol>
           /// <li>Sets the fields to the parameter values.</li>
           /// <li><!--StartFragment-->Attaches the OnDocumentCompleted event handler to the webBrowser</li>
           /// <li>Instantiates the Timer, sets the interval to the configured &quot;poll_interval&quot; value and attaches the OnUpdatesPolling handler method to the Tick event.</li>
           /// </ol>
           /// <p></p>
           /// <p></p>
           /// <p></p>           /// </summary>
           /// <exception>ConfigurationException to signal problems with the configuration file like missing properties.</exception>
           /// <exception>ArgumentNullException if any parameter is null.</exception>
           /// <param name='webBrowser'>The web browser reference obtained in the WebBrowserSite.SetSite method.</param>
           /// <param name='bloomFilter'>The bloom filter instance to use.</param>
           /// <param name='persistence'>The IPersistence instance to use.</param>
           /// <param name='webBrowserWindowNavigator'>The IWebBrowserNavigator instance to use.</param>
           /// <param name='eventsManager'>The IExtensionEventsManager to use.</param>
           /// <param name='browserCustomization'>The IDocHostUIHandler instance to use.</param>
           /// <param name='scriptingObject'>The scripting object to use.</param>
           public MsieClientLogic(Orpheus.Plugin.InternetExplorer.Interop.WebBrowserClass webBrowser, TopCoder.Util.BloomFilter.BloomFilter bloomFilter, Orpheus.Plugin.InternetExplorer.IPersistence persistence, Orpheus.Plugin.InternetExplorer.IWebBrowserWindowNavigator webBrowserWindowNavigator, Orpheus.Plugin.InternetExplorer.IExtensionEventsManager eventsManager, Orpheus.Plugin.InternetExplorer.Interop.IDocHostUIHandler browserCustomization, object scriptingObject) {
           }

           /// Operation OnDocumentCompleted
           /// <summary>
           /// <p><!--StartFragment-->This event handler method is invoked by the host web browser when a new page is displayed. As a result this method will fire a corresponding event (PageChanged).</p>
           /// <p><!--EndFragment--></p>           /// </summary>
           /// <exception>FireEventException propagated from the IExtensionEventsManager</exception>
           /// <param name='pDisp'>Pointer to the IDispatch interface of the window or frame in which the document has loaded</param>
           /// <param name='ref_url'>Pointer to a VARIANT structure of type VT_BSTR that specifies the URL, Universal Naming Convention (UNC) file name, or pointer to an item identifier list (PIDL) of the loaded document.  </param>
           public void OnDocumentCompleted(object pDisp, object ref_url) {
           
        // your code here
           
           }

           /// Operation OnUpdatesPolling
           /// <summary>
           /// <p><!--StartFragment-->This event handler is invoked by the timer when the Tick event is fired.</p>
           /// <p>As a result this method will fire a corresponding event (PollUpdates).</p>
           /// <p></p>
           /// <p><!--EndFragment--></p>           /// </summary>
           /// <exception>FireEventException propagated from the IExtensionEventsManager</exception>
           /// <param name='sender'>The tick events sender</param>
           /// <param name='args'>Tick event args.</param>
           public void OnUpdatesPolling(object sender, System.EventArgs args) {
           
        // your code here
           
           }

           /// Operation CustomizeWebBrowser
           /// <summary>
           /// <p><!--StartFragment-->This method is intended to be used by client code. When the client logic (this class) is incorporated inside an Internet Explorer extension, the web browser will also require customization like providing a scripting object and changing the context menu.</p>
           /// <p class="MsoNormal">On initialization, MSHTML calls QueryInterface on the host's client site, requesting an <em style="mso-bidi-font-style:normal"><span style="mso-bidi-font-weight:bold">IDocHostUIHandler</span></em> interface. If available, MSHTML will call <em style="mso-bidi-font-style:normal"><span style="mso-bidi-font-weight:bold">IDocHostUIHandler</span></em> methods at appropriate times during the lifetime of the MSHTML component.</p>
           /// <p class="MsoNormal">This method provides the IDocHostUIHandler implementation to the browser. The default implementation provided only provides the scripting object.<!--EndFragment--></p>
           /// <p><strong>Implemetation details:</strong></p>
           /// <p><!--StartFragment-->ICustomDoc customDoc = (ICustomDoc)webBrowser.Document;<span style="mso-tab-count:1">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>customDoc.SetUIHandler((IDocHostUIHandler) this.browserCustomization);<!--EndFragment--></p>           /// </summary>
           /// <exception>WebBrowserCustomizationException if can not set the the customization object.</exception>
           /// <exception>ArgumentNullException if parameter is null.</exception>
           /// <param name='webBrowser'>The web browser to customize with the class IDocHostUIHandler member.</param>
           public void CustomizeWebBrowser(Orpheus.Plugin.InternetExplorer.Interop.WebBrowserClass webBrowser) {
           
        // your code here
           
           }

           /// Operation GetInstance
           /// <summary>
           /// <p>This method returns a singleton instance of this class. It creates the singleton using the given parameter.</p>
           /// <p>This method also locks on instance when checking if null and creating the instance.</p>           /// </summary>
           /// <exception>ArgumentNullException if parameter is null.</exception>
           /// <param name='webBrowser'>The web browser reference to pass to the constructor.</param>
           /// <returns>The singleton instance.</returns>
           public Orpheus.Plugin.InternetExplorer.MsieClientLogic GetInstance(Orpheus.Plugin.InternetExplorer.Interop.WebBrowserClass webBrowser) {
           
        // your code here
        return Orpheus.Plugin.InternetExplorer.MsieClientLogic;
           
           }
         }

       }
     }
   }
 }
