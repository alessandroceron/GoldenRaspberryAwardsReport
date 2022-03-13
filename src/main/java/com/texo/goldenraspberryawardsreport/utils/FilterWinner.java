package com.texo.goldenraspberryawardsreport.utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FilterWinner {

    @NoArgsConstructor
    public static class LongerRangeWinners implements Filter {
        @Override
        public Boolean run(Integer value1, Integer value2) {
            return value1 < value2;
        }
    }

    @NoArgsConstructor
    public static class FastestWinners implements Filter {
        @Override
        public Boolean run(Integer value1, Integer value2) {
            return value1 > value2;
        }
    }

    public interface Filter {
        Boolean run(Integer value1, Integer value2);
    }
}
