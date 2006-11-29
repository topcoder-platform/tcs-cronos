/// CSharpClass File "ScriptingObject.cs" generated by Poseidon for UML.
/// Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
/// Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.

 namespace Orpheus_Client_Logic_for_MSIE {
   namespace Orpheus {
     namespace Plugin {
       namespace InternetExplorer {

         ///  <summary>
         ///  <p class="MsoNormal"><!--StartFragment-->This class represents the object that will be exposed to page JavaScript code.</p>
         ///  <p class="MsoNormal">The browser will invoke on the <em>IDocHostUIHandler</em> the <em>GetExternal</em> method which will provide to the browser the scripting object, which web pages will be able to access through the external object:</p>
         ///  <p class="MsoNormal"><span style='font-size:9.0pt; font-family:"Courier New"'>function testScripting()</span></p>
         ///  <p style="margin-left:.5in" class="MsoNormal"><span style='font-size:9.0pt; font-family:"Courier New"'>{</span></p>
         ///  <p style="margin-left:.5in" class="MsoNormal"><span style='font-size:9.0pt; font-family:"Courier New"'><span style="mso-tab-count:1">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span> <span style="mso-spacerun:yes">&nbsp;</span>//get the scripting object interface</span></p>
         ///  <p style="margin-left:.5in" class="MsoNormal"><span style='font-size:9.0pt; font-family:"Courier New"'><span style="mso-tab-count:1">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span> <span style="mso-spacerun:yes">&nbsp;</span>var scriptingObject = window.external;</span></p>
         ///  <p style="margin-left:.5in" class="MsoNormal"><span style='font-size:9.0pt; font-family:"Courier New"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;//invoke it</span></p>
         ///  <p style="margin-left:.5in" class="MsoNormal"><span style='font-size:9.0pt; font-family:"Courier New"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;scriptingObject.LoggedIn();<br/>
         ///   }</span></p>
         ///  <p style="margin-left:.5in" class="MsoNormal">This class provides all the methods required to be accessible from JavaScript.</p>
         ///  <p style="margin-left:.5in" class="MsoNormal">Scripting objects are created by the <em>MsieClientLogic</em> which uses the Object Factory to create the object using a (<em>MsieClientLogic</em>) constructor. Scripting objects must be set the Com visible attribute set to true.</p>
         ///  <p style="mso-margin-top-alt:auto;mso-margin-bottom-alt:auto; text-indent:.5in" class="MsoNormal">This class should be marked with this attribute: [ComVisible(true)].</p>
         ///  <p class="MsoNormal"><!--EndFragment--></p>
         ///  <p><strong>Thread safety: </strong>This class has no mutable state and is thread safe.</p>         ///  </summary>
         public class ScriptingObject {

           /// Attributes

           /// Attribute context
           /// <summary>
           /// <p>Represents the context of the current extension. Most scripting methods will use this context object to get access to one of the context objects.</p>
           /// <p>This field is set in the constructor and not changed afterwards. Can not be null.</p>           /// </summary>
           private final readonly Orpheus.Plugin.InternetExplorer.MsieClientLogic context = <<constructor>>;

           /// Attributes - AssociationEnd

           /// Operations

           /// Constructor ScriptingObject
           /// <summary>
           /// <p>Constructor.</p>
           /// <p>This constructor is required in order for the MsieClientLogic class to be able to create instances of this class, which invokes this constructor passing a reference to itself.</p>
           /// <p><strong>Implementation details:</strong></p>
           /// <p>Sets the field to the parameter value.</p>           /// </summary>
           /// <exception>ArgumentNullException if parameter is null.</exception>
           /// <param name='context'>The extension context object</param>
           public ScriptingObject(Orpheus.Plugin.InternetExplorer.MsieClientLogic context) {
           }

           /// Operation LoggedIn
           /// <summary>
           /// <p>This method will allow for JavaScript code to signal to this extension that a user has logged in.</p>
           /// <p><strong>Implementation details:</strong></p>
           /// <p>Creates a new ExtensionEventArgs class and fires the &quot;LoggedIn&quot; event.</p>           /// </summary>
           /// <exception>FireEventException propagated from the IExtensionEventsManager.</exception>
           public void LoggedIn() {
           
        // your code here
           
           }

           /// Operation LoggedOut
           /// <summary>
           /// <p>This method will allow for JavaScript code to signal to this extension that a user has logged out.</p>
           /// <p><strong>Implementation details:</strong></p>
           /// <p>Creates a new ExtensionEventArgs class and fires the &quot;LoggedOut&quot; event.</p>
           /// <p></p>           /// </summary>
           /// <exception>FireEventException propagated from the IExtensionEventsManager.</exception>
           public void LoggedOut() {
           
        // your code here
           
           }

           /// Operation SetWorkingGame
           /// <summary>
           /// <p>This method will allow for JavaScript code to set the current game id.</p>
           /// <p><strong>Implementation details:</strong></p>
           /// <p>Uses the IPersistence from the context object to store the string representation of the id, using an arbitrary key.</p>
           /// <p>Creates a new ExtensionEventArgs class and fires the &quot;WorkingGameChanged&quot; event.</p>           /// </summary>
           /// <exception>FireEventException propagated from the IExtensionEventsManager.</exception>
           /// <exception>PersistenceException propagated from the IPersistence instance used.</exception>
           /// <param name='gameId'>Game id.</param>
           public void SetWorkingGame(long gameId) {
           
        // your code here
           
           }

           /// Operation GetWorkingGame
           /// <summary>
           /// <p>This method will allow for JavaScript code to get the current game id.</p>
           /// <p><strong>Implementation details:</strong></p>
           /// <p>Uses the IPersistence from the context object to get the string representation of the game id.</p>
           /// <p></p>           /// </summary>
           /// <exception>PersistenceException propagated from the IPersistence instance used.</exception>
           /// <returns>Working game id.</returns>
           public long GetWorkingGame() {
           
        // your code here
        return long;
           
           }

           /// Operation SetCurrentTarget
           /// <summary>
           /// <p>This method will allow for JavaScript code to set the SHA-1 hash of the text of the current target identifier, in the form of a 40-character string of hexadecimal digits, and in integer sequence number.</p>
           /// <p><strong>Implementation details:</strong></p>
           /// <p>Uses the IPersistence from the context object to store the string representation of these values, using arbitrary keys.</p>
           /// <p></p>           /// </summary>
           /// <exception>ArgumentNullException if parameter is null.</exception>
           /// <exception>ArgumentException if parameter is empty string.</exception>
           /// <exception>PersistenceException propagated from the IPersistence instance used.</exception>
           /// <param name='hash'>Target hash.</param>
           /// <param name='sequence'>Sequence number.</param>
           public void SetCurrentTarget(string hash, int sequence) {
           
        // your code here
           
           }

           /// Operation PollMessages
           /// <summary>
           /// <p>This method will allow for JavaScript code to force the poll for updates on the server.</p>
           /// <p><strong>Implementation details:</strong></p>
           /// <p>Creates a new ExtensionEventArgs class and fires the &quot;PollUpdates&quot; event.</p>
           /// <p></p>           /// </summary>
           /// <exception>FireEventException propagated from the IExtensionEventsManager.</exception>
           public void PollMessages() {
           
        // your code here
           
           }

           /// Operation IsPopup__
           /// <summary>
           /// <p>This method will allow for JavaScript code to check whether the browser window containing the current document is a popup window opened by this component.</p>
           /// <p>Compares the browser window that this extension is attached to, to the given window.</p>
           /// <p><strong>Implementation details:</strong></p>
           /// <p>Gets the host browser document: IHTMLDocument2 document = context.WebBrowser.Document;</p>
           /// <p>Check if the current location is not null: document.GetLocation() and if not:</p>
           /// <p>Get the IHTMLWindow2 from the document: document.GetParentWindow();</p>
           /// <p>If this window equals the given window return true.</p>
           /// <p></p>
           /// <p></p>
           /// <p></p>           /// </summary>
           /// <exception>ArgumentNullException if parameter is null.</exception>
           /// <param name='window'>Window pointer to the page the JavaScript code is calling from.</param>
           /// <returns>False if the given window is the main browser window; true otherwise;</returns>
           public bool IsPopup__(Orpheus.Plugin.InternetExplorer.Interop.IHTMLWindow2 window) {
           
        // your code here
        return bool;
           
           }
         }

       }
     }
   }
 }
