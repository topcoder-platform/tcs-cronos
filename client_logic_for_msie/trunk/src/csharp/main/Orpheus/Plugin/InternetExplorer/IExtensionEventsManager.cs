
/// CSharpInterface File "IExtensionEventsManager.cs" generated by Poseidon for UML.
/// Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
/// Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.

 namespace Orpheus_Client_Logic_for_MSIE {
   namespace Orpheus {
     namespace Plugin {
       namespace InternetExplorer {

         ///  <summary>
         ///  <!--StartFragment-->This interface defines the contract that is required for any extension events manager to implement. An events manager is responsible for managing the delegates for every specific event identified by a name and to invoke them when that specific event is fired.
         ///  <p><strong>Thread safety: </strong>Implemetations of this interface are expected to be thread safe.<!--EndFragment--></p>         ///  </summary>
         public interface IExtensionEventsManager {

           /// Inner Classifiers

           ///  <summary>
           ///  </summary>
           public class ExtensionEventHandlerDelegate[] {
           };



           /// Operations

           /// Operation AddEventHandler
           /// <summary>
           /// <p>This is the declaration of the method that registers a new delegate for the specifed event.</p>           /// </summary>
           /// <exception>ArgumentNullException if any parameter is null.</exception>
           /// <exception>ArgumentException if parameter is empty string.</exception>
           /// <param name='eventName'>The name of the event to add the handler to.</param>
           /// <param name='eventHandler'>Event handler delegate.</param>
           /// <returns>True if handler added; false otherwise.</returns>
           bool AddEventHandler(string eventName, Orpheus.Plugin.InternetExplorer.ExtensionEventHandlerDelegate eventHandler) {
           }

           /// Operation RemoveEventHandler
           /// <summary>
           /// <p>This is the declaration of the method that removes a delegate for the specifed event.</p>           /// </summary>
           /// <exception>ArgumentNullException if any parameter is null.</exception>
           /// <exception>ArgumentException if parameter is empty string.</exception>
           /// <param name='eventName'>The name of the event to remove the handler from.</param>
           /// <param name='eventHandler'>Event handler delegate.</param>
           /// <returns>True if handler removed; false otherwise.</returns>
           bool RemoveEventHandler(string eventName, Orpheus.Plugin.InternetExplorer.ExtensionEventHandlerDelegate eventHandler) {
           }

           /// Operation GetEventHandlers
           /// <summary>
           /// <p>This is the declaration of the method that should invoke all delegates associated with the event to fire.</p>
           /// <p></p>           /// </summary>
           /// <exception>ArgumentNullException if any parameter is null.</exception>
           /// <exception>ArgumentException if parameter is empty string.</exception>
           /// <param name='eventName'>The name of the event.</param>
           /// <returns>The array of delegates registered for the current event. Empty array is returned if none are registered.</returns>
           Orpheus.Plugin.InternetExplorer.IExtensionEventsManager.ExtensionEventHandlerDelegate[] GetEventHandlers(string eventName) {
           }

           /// Operation FireEvent
           /// <summary>
           /// <p>This is the declaration of the method that should invoke all delegates associated with the event to fire.</p>
           /// <p></p>           /// </summary>
           /// <exception>ArgumentNullException if any parameter is null.</exception>
           /// <exception>ArgumentException if parameter is empty string.</exception>
           /// <exception>FireEventException if anything goes wrong when firing the event.</exception>
           /// <param name='eventName'>The name of the event to fire.</param>
           /// <param name='sender'>The sender object to pass to handlers.</param>
           /// <param name='args'>The args object to pass to handlers.</param>
           void FireEvent(string eventName, object sender, Orpheus.Plugin.InternetExplorer.ExtensionEventArgs args) {
           }
         }

       }
     }
   }
 }
