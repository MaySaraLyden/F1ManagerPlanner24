package app.data;
public class DataTest{
    public static void main(String[] args){
        Projects projects = new Projects();
        /*projects.createProjectBlock();
        projects.getProjectBlock(0).printProject();
        projects.updateProjectBlock(0, "Design", "Front Wing", "Intense");
        projects.getProjectBlock(0).printProject();
        projects.updateProjectBlock(1, "Design", "Suspension", "Rushed");
        projects.getProjectBlock(1).printProject();
        projects.printAllProjects();*/

        projects.createDesignBlock(1, "Rear Wing", "Rushed");
        projects.createManufactureBlock(2, "Rear Wing", "Normal", 3);
        projects.createResearchBlock(1, "Underfloor", "Intense");
        projects.printAllProjects();

        projects.deleteProjectBlock(1);
        projects.moveBlock(0, 12);
        projects.printAllProjects();
        
        for(int i = 1; i <= 12; i+=1){
            System.out.println("Month " + i);
            projects.getBlocksForMonth(i);
            System.out.println("-----------");
        }
    }
}