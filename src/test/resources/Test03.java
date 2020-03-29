import java.util.HashMap;
import java.util.Map;

public class Test03 implements Runnable {
    public class Solution {
        public int[] twoSum(int[] nums, int target) {
            Map<Integer, Integer> map = new HashMap<>();
            for (int i = 0; i < nums.length; i++) {
                if (map.get(nums[i]) != null) {
                    return new int[]{map.get(nums[i]), i};
                } else {
                    map.put(target - nums[i], i);
                }
            }
            return new int[]{};
        }
    }

    @Override
    public void run() {
        System.out.println("Started to run code ...");
    }
}
