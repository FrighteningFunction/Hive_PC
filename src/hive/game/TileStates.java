package hive.game;

/**
 * Egy Tile lehetséges állapotai.
 * UNSELECTED: a normál állapot
 * SELECTED: a játékos kiválasztotta
 * PINGED: a játékos számára kijelölt lehetőségként
 * TERMINATED: kitörölt vagy törlésre ítélt
 */
public enum TileStates {
    UNSELECTED,
    SELECTED,
    PINGED,
    TERMINATED
}
