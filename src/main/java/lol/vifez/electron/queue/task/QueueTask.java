package lol.vifez.electron.queue.task;

import lol.vifez.electron.kit.Kit;
import lol.vifez.electron.queue.Queue;
import lol.vifez.electron.queue.QueueManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

/*
 * Copyright (c) 2025 Vifez. All rights reserved.
 * Unauthorized use or distribution is prohibited.
 */

@RequiredArgsConstructor
public class QueueTask extends BukkitRunnable {

    private final QueueManager queueManager;

    @Override
    public void run() {
        for (Kit kit : queueManager.getInstance().getKitManager().getKits().values()) {
            if (queueManager.getQueue(kit, false) == null) {
                queueManager.getQueueMap().put(kit.getName(), new Queue(queueManager.getInstance(), kit));

                if (kit.isRanked()) {
                    queueManager.getQueueMap().put("ranked_" + kit.getName(), new Queue(queueManager.getInstance(), kit));
                }
            }
        }

        queueManager.getQueueMap().values().forEach(Queue::move);
    }
}
