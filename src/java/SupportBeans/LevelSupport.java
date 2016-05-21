
package SupportBeans;

/**
 *
 * @author kabugi
 */
public class LevelSupport {
    
    private String treeEntry;
    private int level;

    public LevelSupport(String treeEntry, int level) {
        this.treeEntry = treeEntry;
        this.level = level;
    }

    public LevelSupport() {
    }

    
    public String getTreeEntry() {
        return treeEntry;
    }

    public void setTreeEntry(String treeEntry) {
        this.treeEntry = treeEntry;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    
    
    
    
}
