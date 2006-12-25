
package com.topcoder.util.puzzle;
import com.topcoder.util.puzzle.PuzzleException;
/**
 * <p> An interface defining the behavior of objects that serve as registries or factories of PuzzleType objects.
 * </p>
 * 
 * 
 * @poseidon-object-id [Ifdeeecm10df11a47b1mm6eff]
 */
public interface PuzzleTypeSource {
/**
 * <p>Provides the named PuzzleType object, if it is available.
 * User can check availability through the hasPuzzleType method first.
 * </p>
 * 
 * @poseidon-object-id [Ifdeeecm10df11a47b1mm6ed8]
 * @param name name of the puzzle type
 * @return the named PuzzleType
 * @throws PuzzleException if the named puzzle type does not exist or cannot be provided because of a checked exception
 * @throws IllegalArgumentException if the argument is null
 */
    public com.topcoder.util.puzzle.PuzzleType getPuzzleType(String name);
/**
 * <p>Determines whether a puzzle type corresponding to the specified name is available from this source.
 * </p>
 * 
 * @poseidon-object-id [Ifdeeecm10df11a47b1mm6e95]
 * @param name name of the puzzle type
 * @return true if a puzzle type corresponding to the given name is available from this resource
 * @throws PuzzleException if a checked exception prevents this method from completing successfully.
 * @throws IllegalArgumentException if the argument is null
 */
    public boolean hasPuzzleType(String name);
}


