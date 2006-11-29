/// CSharpClass File "RegistryPersistence.cs" generated by Poseidon for UML.
/// Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
/// Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.

 namespace Orpheus_Client_Logic_for_MSIE {
   namespace Orpheus {
     namespace Plugin {
       namespace InternetExplorer {
         namespace Persistence {

           ///  <summary>
           ///  <!--StartFragment-->This class is an implementation of the IPersistence interface.
           ///  <p class="MsoNormal">It used Registry to save the values.</p>
           ///  <p><strong>Thread safety: </strong>This class has no state and is thread safe.</p>
           ///  <!--EndFragment-->           ///  </summary>
           public class RegistryPersistence : Orpheus.Plugin.InternetExplorer.IPersistence {

             /// Operations

             /// Constructor RegistryPersistence
             /// <summary>
             /// <p>Empty Constructor.</p>
             /// <p></p>             /// </summary>
             public RegistryPersistence() {
             }

             /// Operation this
             /// <summary>
             /// <p>This indexer saves and returns a value for a specified key from the registry.</p>
             /// <p><strong>Implementation details:</strong></p>
             /// <p>Getter:</p>
             /// <p>Returns the value for the specified key from the registry.</p>
             /// <p>Gets the current user registry key:&nbsp; <span style="font-family:Verdana; mso-bidi-font-family:Verdana">RegistryKey rk = Registry.CurrentUser</span></p>
             /// <p>Gets the subkey for this application(named for example Orpheus) using the OpenSubkey method and gets the value from this registry key for the indexer key. If no registry key is found or value empty string is returned.</p>
             /// <p>Closes the registry key.</p>
             /// <p>Setter:</p>
             /// <p>Sets the value for the specified key in the registry.</p>
             /// <p>Gets the current user registry key:&nbsp; <span style="font-family:Verdana; mso-bidi-font-family:Verdana">RegistryKey rk = Registry.CurrentUser</span></p>
             /// <p>Gets the subkey for this application(Orpheus), creates one if not found using the CreateSubkey method and sets the value for the &quot;key&quot;.</p>
             /// <p>Closes the registry key.</p>             /// </summary>
             /// <exception>Setter - ArgumentNullException if parameter is null.</exception>
             /// <exception>Setter -ArgumentException if parameter is empty string.</exception>
             /// <exception>PersistenceException to wrap any problems with the registry.</exception>
             /// <param name='key'>Key under which the value is stored.</param>
             /// <returns>The value.</returns>
             public string this(string key) {
             
        // your code here
        return string;
             
             }
           }

         }
       }
     }
   }
 }
