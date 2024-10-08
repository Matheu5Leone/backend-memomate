package memo.backend.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NoteColorEnum {

    DEFAULT("#FFFFFF"),
    PURPLE("#D3A4E2"),
    YELLOW("#E2DBA4"),
    CYAN("#A4E2D8"),
    GREEN("ABE2A4"),
    BLUE("#7AA9FF");

    private final String value;

}
