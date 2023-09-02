package com.github.unldenis.hologram.line.hologram;

import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.line.ILine;
import com.github.unldenis.hologram.line.ITextLine;
import com.github.unldenis.hologram.line.TextLine;
import org.bukkit.Location;

import java.util.Arrays;

public class TextSequentialLoader implements IHologramLoader {

  @Override
  public void load(Hologram hologram, ILine<?>[] lines) {
    set(hologram, lines, true);
  }

  @Override
  public void teleport(Hologram hologram) {
    set(hologram, hologram.getLines().toArray(new ILine<?>[0]), false);
    // TODO: When teleporting, the holograms unexpectedly become distant. Understand why.
  }

  private void set(Hologram hologram, ILine<?>[] lines, boolean add) {
    Location cloned = hologram.getLocation().clone();
    Arrays.stream(lines).forEach(line -> {
      switch (line.getType()) {
        case TEXT_LINE:
        case TEXT_ANIMATED_LINE:
        case CLICKABLE_TEXT_LINE:
          TextLine tL = ((ITextLine) line).asTextLine();

          // add to lines
          tL.setLocation(cloned.clone());

          if(add) {
            hologram.getLines().add(0, tL);
          } else {
            hologram.getSeeingPlayers().forEach(tL::teleport);
          }
          cloned.setZ(cloned.getZ() + 0.175 * tL.getObj().length());
          break;
        default:
          throw new RuntimeException("This method load supports only TextLine & TextALine & ClickableTextLine.");
      }
    });
  }
}