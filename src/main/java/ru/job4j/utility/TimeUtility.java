package ru.job4j.utility;

import ru.job4j.model.Post;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

public class TimeUtility {

    public static List<TimeZone> getAllTimeZones() {
        var zones = new ArrayList<TimeZone>();
        for (String timeId : TimeZone.getAvailableIDs()) {
            zones.add(TimeZone.getTimeZone(timeId));
        }
        return zones;
    }

    public static TimeZone getDefaultTimeZone() {
        return TimeZone.getDefault();
    }

    public static LocalDateTime convertToUserTimeZone(LocalDateTime taskTime, String userTimezone) {
        return taskTime
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of(userTimezone))
                .toLocalDateTime();
    }

    public static Post convertTaskToUserTimeZone(Post post, String userTimezone) {
        post.setCreated(convertToUserTimeZone(post.getCreated(), userTimezone));
        return post;
    }

    public static Collection<Post> convertTasksToUserTimeZone(Collection<Post> posts, String userTimezone) {
        if (posts == null || posts.isEmpty()) {
            return posts;
        }
        for (Post task : posts) {
            if (task.getCreated() != null) {
                task.setCreated(convertToUserTimeZone(task.getCreated(), userTimezone));
            }
        }
        return posts;
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
