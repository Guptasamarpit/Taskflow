package com.taskflow.dto;

public final class DashboardDtos {
    private DashboardDtos() {
    }

    public record DashboardResponse(long projectCount, long taskCount, long todoCount, long inProgressCount,
            long doneCount, long highPriorityCount) {
    }
}
