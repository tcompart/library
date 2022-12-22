package de.tcompart.library.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;

class EventTest {

  @Test
  void twoAreEqual() {
    assertThat(new Event()).isEqualTo(new Event());
  }


  @Test
  void objectsAreEquivalentForSets() {
    assertThat(Sets.newSet(new Event(), new Event(), new Event())).hasSize(1);
  }

}