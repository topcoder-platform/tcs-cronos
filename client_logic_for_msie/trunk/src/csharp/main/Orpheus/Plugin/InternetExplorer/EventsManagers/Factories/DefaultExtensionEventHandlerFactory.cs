/// CSharpClass File "DefaultExtensionEventHandlerFactory.cs" generated by Poseidon for UML.
/// Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
/// Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.

 namespace Orpheus_Client_Logic_for_MSIE {
   namespace Orpheus {
     namespace Plugin {
       namespace InternetExplorer {
         namespace EventsManagers {
           namespace Factories {

             ///  <summary>
             ///  <!--StartFragment-->This class is an implementation of the IExtensionEventHandlerFactory interface.
             ///  <p style="mso-margin-top-alt:auto;mso-margin-bottom-alt:auto" class="MsoNormal">This implementation will use the specified event name to get from the configuration file a value representing a key. This key will be used by the Object Factory component to create the required IExtensionEventHandler implementation. By using Object Factory component, constructor parameter values can be specified for the created implementation. The details of how to configure the Object Factory component can be found in the component&rsquo;s specification document.</p>
             ///  <p><strong>Thread safety: </strong>This class has no state and is thread safe.</p>
             ///  <!--EndFragment-->             ///  </summary>
             public class DefaultExtensionEventHandlerFactory : Orpheus.Plugin.InternetExplorer.EventsManagers.IExtensionEventHandlerFactory {

               /// Inner Classifiers

               ///  <summary>
               ///  </summary>
               public class IExtensionEventHandler[] {
               };

               /// Attributes

               /// Attribute DefaultConfigurationNamespace
               /// <summary>
               /// <p>Represents the default configuration namespace.</p>               /// </summary>
               public string DefaultConfigurationNamespace = "Orpheus.Plugin.InternetExplorer.EventsManagers.Factories";

               /// Attribute DefaultObjectFactoryNamespace
               /// <summary>
               /// <p>Represents the default Object Factory component namespace.</p>
               /// <p></p>               /// </summary>
               public string DefaultObjectFactoryNamespace = "TopCoder.Util.ObjectFactory";

               /// Attribute configurationNamespace
               /// <summary>
               /// <p>Represents the configuration namespace to use.</p>
               /// <p>Set in the constructor and not changed afterwards. Can not ne null or empty string.</p>               /// </summary>
               private final readonly string configurationNamespace = <<constructor>>;

               /// Attribute objectFactoryNamespace
               /// <summary>
               /// <p>Represents the Object Factory namespace to use.</p>
               /// <p>Set in the constructor and not changed afterwards. Can not ne null or empty string.</p>
               /// <p></p>               /// </summary>
               private final readonly string objectFactoryNamespace = <<constructor>>;

               /// Operations

               /// Constructor DefaultExtensionEventHandlerFactory
               /// <summary>
               /// <p>Constructor.</p>
               /// <p>Sets the configuration namespeces to the default.</p>
               /// <p></p>               /// </summary>
               public DefaultExtensionEventHandlerFactory() {
               }

               /// Constructor DefaultExtensionEventHandlerFactory
               /// <summary>
               /// <p>Constructor.</p>
               /// <p>Sets the fields to the parameter values.</p>
               /// <p></p>
               /// <p></p>               /// </summary>
               /// <exception>ArgumentNullException if any parameter is null.</exception>
               /// <exception>ArgumentException if parameter is empty string.</exception>
               /// <param name='configurationNamespace'>Custom configuration namespace.</param>
               /// <param name='objectFactoryNamespace'>Custom Object Factory namespace.</param>
               public DefaultExtensionEventHandlerFactory(string configurationNamespace, string objectFactoryNamespace) {
               }

               /// Operation CreateHandlers
               /// <summary>
               /// <p>Reads the key from the configuration file and uses the Object Factory to create the event handler instances.</p>
               /// <p><strong>Implementation details:</strong></p>
               /// <ol>
               /// <li>
               /// Reads from the configuration file the &lt;event_name&gt;_handlers property and for each one:
               /// <ol>
               /// <li>Reads from the configuration file the &lt;handler name&gt;_handler propery and uses it to create the instance using the Object Factory.</li>
               /// </ol>
               /// </li>
               /// <li>Returns an array of handlers. Empty array if none are registered.</li>
               /// </ol>               /// </summary>
               /// <exception>ArgumentNullException if parameter is null.</exception>
               /// <exception>ArgumentException if parameter is empty string.</exception>
               /// <exception>EventHandlerCreationException to signal problems if can not create the event handlers.</exception>
               /// <param name='eventName'>Event name.</param>
               /// <returns>Event handlers.</returns>
               public Orpheus.Plugin.InternetExplorer.EventsManagers.Factories.DefaultExtensionEventHandlerFactory.IExtensionEventHandler[] CreateHandlers(string eventName) {
               
        // your code here
        return Orpheus.Plugin.InternetExplorer.EventsManagers.Factories.DefaultExtensionEventHandlerFactory.IExtensionEventHandler[];
               
               }
             }

           }
         }
       }
     }
   }
 }
