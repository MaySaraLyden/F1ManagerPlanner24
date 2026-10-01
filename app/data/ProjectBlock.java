package app.data;

public abstract class ProjectBlock {

    private int index;
    private String projectType;
    private String part;
    private String approach;
    private int month;
    private boolean isComplete;
    private int cost;
    
    public ProjectBlock(int index, int month){
        this.index = index;
        this.month = month;
        projectType = "Design";
        part = "Chassis";
        approach = "Normal";
        isComplete = false;
        calcCost();
    }

    public ProjectBlock(int index, int month, String projectType, String part, String approach){
        this.index = index;
        this.projectType = projectType;
        this.part = part;
        this.approach = approach;
        this.month = month;
        isComplete = false;
    }

    // Setters

    public void setIndex(int index) {
        this.index = index;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }
    
    public void setPart(String part) {
        this.part = part;
        calcCost();
    }

    public void setApproach(String approach) {
        this.approach = approach;
        calcCost();
    }

    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public abstract void calcCost();

    // Getters

    public String getPart(){
        return part;
    }

    public String getApproach(){
        return approach;
    }

    public int getMonth(){
        return month;
    }

    public int getCost(){
        return cost;
    }

    public String getProjectType(){
        return projectType;
    }

    public int getIndex(){
        return index;
    }

    public boolean isComplete(){
        return isComplete;
    }

    // Print Methods

    public void printProject(){
        printProjectTitle();
        System.out.println("Part: " + part);
        System.out.println("Approach: " + approach);
        System.out.println("Project Cost: " + cost);
    }

    protected void printProjectTitle(){
        System.out.println(index + " Project");
    }
}
