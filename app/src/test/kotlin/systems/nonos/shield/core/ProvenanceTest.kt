package systems.nonos.shield.core

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Reading back which core a build carries. The reader survives a missing, short or reordered file
 * without inventing a commit, since a plausible wrong commit sends a bug report to the wrong code.
 */
class ProvenanceTest {
    @Test
    fun the_three_fields_are_read_in_any_order() {
        val read = Provenance.read("ndk 27.0.1\nrustc 1.91.1\nstate clean\ncommit 2433e7a\n")
        assertEquals(Provenance("2433e7a", "clean", "1.91.1", "27.0.1"), read)
    }

    @Test
    fun a_clean_checkout_shows_the_commit_alone() {
        assertEquals("2433e7a", Provenance("2433e7a", "clean", "1.91.1", "27.0.1").line)
    }

    @Test
    fun a_modified_checkout_says_so_beside_it() {
        assertEquals(
            "2433e7a (modified)",
            Provenance("2433e7a", "modified", "1.91.1", "27.0.1").line,
        )
    }

    @Test
    fun a_missing_field_is_unknown_rather_than_invented() {
        assertEquals(Provenance("unknown", "unknown", "unknown", "unknown"), Provenance.read(""))
        assertEquals("unknown", Provenance.read("commit\nstate\n").commit)
    }
}
