package cn.crowdos.kernel.resource;

import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.constraint.Condition;
import cn.crowdos.kernel.constraint.Constraint;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractTask implements Task{

    protected final long taskId;
    private static long taskCounter = 1;
    protected final List<Constraint> constraints;
    protected final TaskDistributionType taskDistributionType;
    protected TaskStatus status;
    protected  TaskPriority priority;

    //new
    protected AbstractTask(List<Constraint> constraints, TaskDistributionType taskDistributionType,TaskPriority priority) {
        this.taskId = taskCounter++;
        this.constraints = constraints;
        this.taskDistributionType = taskDistributionType;
        this.priority = priority;
    }
    //new_end

    protected AbstractTask(List<Constraint> constraints, TaskDistributionType taskDistributionType) {
        this.taskId = taskCounter++;
        this.constraints = constraints;
        this.taskDistributionType = taskDistributionType;
        this.priority = TaskPriority.MEDIUM;
    }

    abstract public Decomposer<Task> decomposer();

    public long getTaskId() {
        return this.taskId;
    }

    @Override
    public TaskDistributionType getTaskDistributionType() {
        return taskDistributionType;
    }

    @Override
    public TaskStatus getTaskStatus() {
        return status;
    }

    //new
    @Override
    public TaskPriority getTaskPriority() {
        return priority;
    }
    //new_end

    @Override
    public void setTaskStatus(TaskStatus status) {
        this.status = status;
    }

    //new
    @Override
    public void setTaskPriority(TaskPriority priority){
        this.priority = priority;
    }
    //new_end

    @Override
    public List<Constraint> constraints() {
        return constraints;
    }

    @Override
    public boolean canAssignTo(Participant participant) {
        for (Constraint constraint : constraints) {
            Class<? extends Condition> conditionClass = constraint.getConditionClass();
            if(participant.hasAbility(conditionClass)){
                if (!constraint.satisfy(participant.getAbility(conditionClass)))
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean assignable() {
        return status == TaskStatus.READY;
    }

    @Override
    public boolean finished() {
        return status == TaskStatus.FINISHED;
    }

}
