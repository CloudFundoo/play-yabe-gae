package lib.jobs;

import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.modules.siena.SienaFixtures;

@OnApplicationStart
public class BootStrap extends Job {

    @Override
    public void doJob() {
        SienaFixtures.deleteAllModels();
        SienaFixtures.loadModels("data.yml");
    }
}