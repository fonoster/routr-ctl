package io.routr.ctl;

import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

public class CmdGet {

    private static CtlUtils ctlUtils;

    CmdGet(Subparsers subparsers, CtlUtils ctlUtils) {
        Subparser get = subparsers.addParser("get").help("display a list of resources");
        get.addArgument("resource").metavar("type").choices("agent", "agents", "peer", "peers",
                "domain", "domains", "number", "numbers", "gateway", "gateways").help("type of resource (ie.: agent, domain, etc)");
        get.addArgument("reference").nargs("?").setDefault("").help("reference to resource");
        get.addArgument("--filter").metavar("filter").setDefault("*").help("apply filter base on resource metadata");
        get.epilog(String.join(
            System.getProperty("line.separator"),
            "Examples:",
            "  # Shows all the agents in the system",
            "  $ rctl get agents\n",
            "  # List a single agent by ref",
            "  $ rctl get agent ag3f77f6\n",
            "  # Gets number using its reference",
            "  $ rctl get numbers --filter \"@.metadata.ref=='dd50baa4'"
        ));
        CmdGet.ctlUtils = ctlUtils;
    }

    void run(String resource, String ref, String filter) {
        if (resource.equals("agent") || resource.equals("agents")) new CmdGetAgents(ctlUtils).printAgents(ref, filter);
        if (resource.equals("peer") || resource.equals("peers")) new CmdGetPeers(ctlUtils).printPeers(ref, filter);
        if (resource.equals("gateway") || resource.equals("gateways")) new CmdGetGateways(ctlUtils).printGateways(ref, filter);
        if (resource.equals("number") || resource.equals("numbers")) new CmdGetNumbers(ctlUtils).printNumbers(ref, filter);
        if (resource.equals("domain") || resource.equals("domains")) new CmdGetDomains(ctlUtils).printDomains(ref, filter);
    }
}
