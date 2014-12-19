package redis.clients.jedis;

import java.util.*;

@Deprecated
public abstract class PipelineBlock extends Pipeline
{
    public abstract void execute();
}
