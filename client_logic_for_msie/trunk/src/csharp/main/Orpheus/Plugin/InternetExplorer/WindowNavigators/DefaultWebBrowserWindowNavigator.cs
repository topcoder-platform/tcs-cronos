/// CSharpClass File "DefaultWebBrowserWindowNavigator.cs" generated by Poseidon for UML.
/// Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
/// Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.

 namespace Orpheus_Client_Logic_for_MSIE {
   namespace Orpheus {
     namespace Plugin {
       namespace InternetExplorer {
         namespace WindowNavigators {

           ///  <summary>
           ///  <!--StartFragment-->This class is an implementation of the IWebBrowserWindowNavigator interface.
           ///  <p class="MsoNormal">It will be responsible for displaying the requested page or the provided content inside either the main browser window or inside a opened and reused window.<!--StartFragment--></p>
           ///  <p>The new opened window will have the same customization object set to it as the main browser window. When the new window is opened, the Internet Explorer will load the extension object again. The extension object, if used as depicted in the component specifications, will get the same instance of the <em style="mso-bidi-font-style:normal">MsieClientLogic</em> by using the singleton of the class, and will call the CustomizeWebBrowser method which will set the same customization object to the newly opened browser.</p>
           ///  <p><!--EndFragment--></p>
           ///  <!--EndFragment--><strong>Thread safety: </strong>This class locks on this inside the property getter and setter and inside the Navigate methods.           ///  </summary>
           public class DefaultWebBrowserWindowNavigator : Orpheus.Plugin.InternetExplorer.IWebBrowserWindowNavigator {

             /// Attributes

             /// Attribute popupWindow
             /// <summary>
             /// <p>Represents the newlly created window. This new window is created using the DOM of the browser inside the Navigate method.</p>
             /// <p>It can be null if no new window is displayed. Once a new one is displayed it will point to this one.</p>             /// </summary>
             private Orpheus.Plugin.InternetExplorer.Interop.IHTMLWindow2 popupWindow;

             /// Attributes - AssociationEnd

             /// Association End popupWindow
             private Orpheus.Plugin.InternetExplorer.Interop.IHTMLWindow2 popupWindow;

             /// Association End webBrowser
             private Orpheus.Plugin.InternetExplorer.Interop.WebBrowserClass webBrowser;

             /// Operations

             /// Constructor DefaultWebBrowserNavigator
             /// <summary>
             /// <p>Empty constructor.</p>             /// </summary>
             public DefaultWebBrowserNavigator() {
             }

             /// Operation Navigate
             /// <summary>
             /// <p>This method directs the browser or the new window to the specified URL.</p>
             /// <p><strong>Implementation details:</strong></p>
             /// <p>1. If the newWindow is false the browser.Navigate method is used to direct the browser to the specified location.</p>
             /// <p>2. If a new window should be used: If the newWindow field is not null and the newWindow is not closed (newWindow.Closed) get the window document (window.Document) and navigate to the specified URL.</p>
             /// <p>If the newWindow is null or closed the window.Open(&ldquo;about:blank&rdquo;) method is used to open a new window and sets the field to the returned reference. It then gets the document and navigates to the specified URL.</p>
             /// <p>The new displayed window should have minimum controls displayed like no toolbar, no status bar.</p>             /// </summary>
             /// <exception>ArgumentNullException if parameter is null.</exception>
             /// <exception>ArgumentException if parameter is empty string.</exception>
             /// <exception>WebBrowserNavigationException to signal problems while navigating to the new location.</exception>
             /// <param name='webBrowser'>The web browser to disaplay the url in, or to open the window from.</param>
             /// <param name='url'>The URL to navigate to .</param>
             /// <param name='newWindow'>Indoicates whether a new window should be opened.</param>
             public void Navigate(Orpheus.Plugin.InternetExplorer.Interop.WebBrowserClass webBrowser, string url, bool newWindow) {
             
        // your code here
             
             }

             /// Operation Navigate
             /// <summary>
             /// <p><!--StartFragment-->This method displys the content in the browser browser or the new window.</p>
             /// <p><strong>Implementation details:</strong></p>
             /// <p>1. If the newWindow is false the browser.Document.Write method is used to set the content <span style="mso-spacerun:yes">&nbsp;</span>of the browser.</p>
             /// <p>2. If a new window should be used: If the newWindow field is not null and the newWindow is not closed (newWindow.Closed) get the window document (window.Document) and write the content.</p>
             /// <p>If the newWindow is null or closed the window.Open(&ldquo;about:blank&rdquo;) method is used to open a new window and sets the field to the returned reference. It then gets the document and writes the content.</p>
             /// <p>The new displayed window should have minimum controls displayed like no toolbar, no status bar.</p>             /// </summary>
             /// <exception>ArgumentNullException if parameter is null.</exception>
             /// <exception>WebBrowserNavigationException to signal problems while setting the content of the new location.</exception>
             /// <param name='webBrowser'>The web browser to disaplay the content in, or to open the window from.</param>
             /// <param name='content'>The content to set to the browser.</param>
             /// <param name='newWindow'>Indoicates whether a new window should be opened.</param>
             public void Navigate(Orpheus.Plugin.InternetExplorer.Interop.WebBrowserClass webBrowser, Orpheus.Plugin.InternetExplorer.IWebBrowserWindowNavigator.Stream content, bool newWindow) {
             
        // your code here
             
             }
           }

         }
       }
     }
   }
 }
