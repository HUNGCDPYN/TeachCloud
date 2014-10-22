/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.designer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import jo.edu.just.network.NetworkDatacenterBroker;
import jo.just.api.BrokerAPI;
import jo.just.api.CloudletAPI;
import jo.just.api.DatacenterAPI;
import jo.just.api.HostAPI;
import jo.just.api.VMAPI;
import jo.just.mapreduce.MapReduceSchedulerSpaceShared;
import jo.just.mapreduce.Master;
import jo.just.mapreduce.Reduce;
import jo.just.node.widget.BrokerNode;
import jo.just.node.widget.CloudletNode;
import jo.just.node.widget.DatacenterNode;
import jo.just.node.widget.HostNode;
import jo.just.node.widget.VMNode;
import jo.just.swf.NetworkWorkloadFileReader;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.HostDynamicWorkload;
import org.cloudbus.cloudsim.HostStateHistoryEntry;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.UtilizationModelNull;
import org.cloudbus.cloudsim.UtilizationModelStochastic;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.VmSchedulerTimeSharedOverSubscription;
import org.cloudbus.cloudsim.VmStateHistoryEntry;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.power.Constants;
import org.cloudbus.cloudsim.network.datacenter.AggregateSwitch;
import org.cloudbus.cloudsim.network.datacenter.EdgeSwitch;

import org.cloudbus.cloudsim.network.datacenter.NetworkCloudlet;
import org.cloudbus.cloudsim.network.datacenter.NetworkCloudletSpaceSharedScheduler;
import org.cloudbus.cloudsim.network.datacenter.NetworkConstants;
import org.cloudbus.cloudsim.network.datacenter.NetworkDatacenter;
import org.cloudbus.cloudsim.network.datacenter.NetworkHost;
import org.cloudbus.cloudsim.network.datacenter.NetworkVm;
import org.cloudbus.cloudsim.network.datacenter.NetworkVmAllocationPolicy;
import org.cloudbus.cloudsim.network.datacenter.RootSwitch;
import org.cloudbus.cloudsim.network.datacenter.TaskStage;
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.power.PowerDatacenterBroker;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.power.PowerVm;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationAbstract;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationInterQuartileRange;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationLocalRegression;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationLocalRegressionRobust;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationMedianAbsoluteDeviation;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationStaticThreshold;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicySimple;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicyMaximumCorrelation;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicyMinimumMigrationTime;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicyMinimumUtilization;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicyRandomSelection;
import org.cloudbus.cloudsim.power.models.PowerModel;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPower;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G4Xeon3040;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisioner;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.util.MathUtil;
import org.cloudbus.cloudsim.util.WorkloadFileReader;
import org.netbeans.api.visual.vmd.VMDNodeWidget;
import org.netbeans.api.visual.vmd.VMDPinWidget;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import radlab.rain.util.RainOlioWorkload;

/**
 *
 * @author apple
 */
public class Interpreter {

    //Datacenter list
    List<Datacenter> datacenters = new ArrayList<Datacenter>();
    List<NetworkDatacenter> networkDatacenters = new ArrayList<NetworkDatacenter>();
    List<PowerDatacenter> powerDatacenters = new ArrayList<PowerDatacenter>();
    //Broker list
    List<DatacenterBroker> datacenterBrokers = new ArrayList<DatacenterBroker>();
    public List<NetworkDatacenterBroker> netDatacenterBrokers = new ArrayList<NetworkDatacenterBroker>();
    List<PowerDatacenterBroker> powerDatacenterBrokers = new ArrayList<PowerDatacenterBroker>();
    //Host list
    List<Host> hosts = new ArrayList<Host>();
    List<NetworkHost> networkHosts = new ArrayList<NetworkHost>();
    List<PowerHost> powerHosts = new ArrayList<PowerHost>();
    //VMM list
    List<Vm> vms = new ArrayList<Vm>();
    List<NetworkVm> networkVms = new ArrayList<NetworkVm>();
    List<PowerVm> powerVms = new ArrayList<PowerVm>();
    //CPU list
    List<Pe> pes = new ArrayList<Pe>();
    //Cloudlet list
    List<Cloudlet> cloudlets = new ArrayList<Cloudlet>();
    List<NetworkCloudlet> networkCloudlets = new ArrayList<NetworkCloudlet>();
    //Node widget list
    List<VMDNodeWidget> nodeWidgets = new ArrayList<VMDNodeWidget>();
    List<VMDNodeWidget> datacenterWidgets = new ArrayList<VMDNodeWidget>();
    List<VMDNodeWidget> brokerWidgets = new ArrayList<VMDNodeWidget>();
    List<VMDNodeWidget> hostWidgets = new ArrayList<VMDNodeWidget>();
    List<VMDNodeWidget> vmWidgets = new ArrayList<VMDNodeWidget>();
    List<VMDNodeWidget> cloudletWidgets = new ArrayList<VMDNodeWidget>();
    //Edge widget list
    List<ConnectionWidget> connectionWidget = new ArrayList<ConnectionWidget>();
    //Map node to datacenter
    Map<Node, Datacenter> mapNodeDatacenter = new HashMap<Node, Datacenter>();
    Map<Node, NetworkDatacenter> mapNodeNetworkDatacenter = new HashMap<Node, NetworkDatacenter>();
    Map<Node, PowerDatacenter> mapNodePowerDatacenter = new HashMap<Node, PowerDatacenter>();
    //Graph Scene
    VCCDGraphScene scene;
    //type
    private static final int NORMAL = 0;
    private static final int NETWORK = 1;
    private static final int POWER = 2;

    public Interpreter(VCCDGraphScene scene) {

        this.scene = scene;

        init();
        int num_user = brokerWidgets.size(); // number of cloud users
        Calendar calendar = Calendar.getInstance();
        boolean trace_flag = false; // mean trace events

        // Initialize the CloudSim library
        CloudSim.init(num_user, calendar, trace_flag);

//        startNormalSimulation();
//        startNetworkSimulation();
        startPowerSimulation();

//        CloudSim.startSimulation();

        //power
        CloudSim.terminateSimulation(24 * 60 * 60);//zax chose from properties
        double lastClock = CloudSim.startSimulation();
        printDebts();
        CloudSim.stopSimulation();

        try {
            printCloudletReceivedList();
            printResults(
                powerDatacenters.get(0),
                vms,
                lastClock,
                "experimentName",
                Constants.OUTPUT_CSV,
                "outputFolder");
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void init() {


        for (Widget layer : scene.getChildren()) {
            LayerWidget layerWidget = (LayerWidget) layer;
            for (Widget widget : layerWidget.getChildren()) {
//                System.err.println("widget = " + widget);

                if (widget instanceof VMDNodeWidget) {
                    if (scene.getNode(widget) instanceof BrokerNode) {
                        brokerWidgets.add((VMDNodeWidget) widget);
                    } else if (scene.getNode(widget) instanceof DatacenterNode) {
                        datacenterWidgets.add((VMDNodeWidget) widget);
                    } else if (scene.getNode(widget) instanceof HostNode) {
                        hostWidgets.add((VMDNodeWidget) widget);
                    } else if (scene.getNode(widget) instanceof VMNode) {
                        vmWidgets.add((VMDNodeWidget) widget);
                    } else if (scene.getNode(widget) instanceof CloudletNode) {
                        cloudletWidgets.add((VMDNodeWidget) widget);
                    }

                    nodeWidgets.add((VMDNodeWidget) widget);

                } else if (widget instanceof ConnectionWidget) {
                    connectionWidget.add((ConnectionWidget) widget);
                }
            }
        }

//        System.err.println("broker = " + brokerWidgets.size());
//        System.err.println("datacenter = " + datacenterWidgets.size());
//        System.err.println("host = " + hostWidgets.size());
//        System.err.println("vm = " + vmWidgets.size());
//        System.err.println("cloudlet = " + cloudletWidgets.size());
//        System.err.println("edge = " + connectionWidget.size());
    }

    private void startNormalSimulation() {
        // Create Datacenter
        for (int i = 0; i < datacenterWidgets.size(); i++) {
            Datacenter ndc = createDatacenter(datacenterWidgets.get(i));
            mapNodeDatacenter.put(scene.getNode(datacenterWidgets.get(i)), ndc);
            datacenters.add(ndc);
        }

        //Create Broker
        for (int i = 0; i < brokerWidgets.size(); i++) {
            datacenterBrokers.add(createDatacenterBroker(brokerWidgets.get(i)));
        }
    }

    private void startNetworkSimulation() {

        // Create Datacenter
        for (int i = 0; i < datacenterWidgets.size(); i++) {
            NetworkDatacenter ndc = createNetworkDatacenter(datacenterWidgets.get(i));
            mapNodeNetworkDatacenter.put(scene.getNode(datacenterWidgets.get(i)), ndc);
            networkDatacenters.add(ndc);
        }

        //Create Broker
        for (int i = 0; i < brokerWidgets.size(); i++) {
            netDatacenterBrokers.add(createNetworkDatacenterBroker(brokerWidgets.get(i)));
        }
    }

    private void startPowerSimulation() {
        // Create Datacenter
        for (int i = 0; i < datacenterWidgets.size(); i++) {
            PowerDatacenter ndc = createPowerDatacenter(datacenterWidgets.get(i));
            mapNodePowerDatacenter.put(scene.getNode(datacenterWidgets.get(i)), ndc);
            powerDatacenters.add(ndc);
        }

        //Create Broker
        for (int i = 0; i < brokerWidgets.size(); i++) {
            powerDatacenterBrokers.add(createPowerDatacenterBroker(brokerWidgets.get(i)));
        }
    }

    private Datacenter createDatacenter(VMDNodeWidget nodeWidget) {
        List<Node> nodeWidgetsConnected = getNodesConnectedTo(nodeWidget);
        List<Host> hostList = new ArrayList<Host>();

        for (Node node : nodeWidgetsConnected) {

            if (node instanceof HostNode) {
                hostList.addAll(createHost((HostNode) node, hostList.size()));
            } else {
                System.err.println("Error: Can not connect " + node.getDisplayName() + "to the " + nodeWidget.getNodeName());
            }
        }

        DatacenterAPI DatacenterInfo = ((DatacenterNode) scene.getNode(nodeWidget)).getObj();
        String arch = DatacenterInfo.getArch(); // system architecture
        String os = DatacenterInfo.getOs(); // operating system
        String vmm = DatacenterInfo.getVmm();
        double time_zone = 10.0;//DatacenterInfo.getTime(); // time zone this resource located
        double cost = DatacenterInfo.getCostPerSecond(); // the cost of using processing in this resource
        double costPerMem = DatacenterInfo.getCostPerMemory(); // the cost of using memory in this resource
        double costPerStorage = DatacenterInfo.getCostPerStorage(); // the cost of using storage in this resource
        double costPerBw = DatacenterInfo.getCostPerBandwidth(); // the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch,
                os,
                vmm,
                hostList,
                time_zone,
                cost,
                costPerMem,
                costPerStorage,
                costPerBw);

        // 6. Finally, we need to create a NetworkDatacenter object.
        Datacenter datacenter = null;
        try {

            datacenter = new Datacenter(
                    DatacenterInfo.getName(),
                    characteristics,
                    new VmAllocationPolicySimple(hostList), //zax: to do select from list
                    storageList,
                    0);//zax: to do schedulingInterval is the delay to perform som action, i need to check it.

        } catch (Exception e) {
            e.printStackTrace();
        }


        return datacenter;
    }

    private PowerDatacenter createPowerDatacenter(VMDNodeWidget nodeWidget) {
        List<Node> nodeWidgetsConnected = getNodesConnectedTo(nodeWidget);
        List<PowerHost> hostList = new ArrayList<PowerHost>();

        for (Node node : nodeWidgetsConnected) {

            if (node instanceof HostNode) {
                hostList.addAll(createPowerHost((HostNode) node, hostList.size()));
            } else {
                System.err.println("Error: Can not connect " + node.getDisplayName() + "to the " + nodeWidget.getNodeName());
            }
        }

        DatacenterAPI DatacenterInfo = ((DatacenterNode) scene.getNode(nodeWidget)).getObj();
        String arch = DatacenterInfo.getArch(); // system architecture
        String os = DatacenterInfo.getOs(); // operating system
        String vmm = DatacenterInfo.getVmm();
        double time_zone = 10.0;//DatacenterInfo.getTime(); // time zone this resource located
        double cost = DatacenterInfo.getCostPerSecond(); // the cost of using processing in this resource
        double costPerMem = DatacenterInfo.getCostPerMemory(); // the cost of using memory in this resource
        double costPerStorage = DatacenterInfo.getCostPerStorage(); // the cost of using storage in this resource
        double costPerBw = DatacenterInfo.getCostPerBandwidth(); // the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN devices by now
        String vmAllocationPolicyType = DatacenterInfo.getVmAllocationPolicy();
        String vmSelectionPolicyType = DatacenterInfo.getVmSelectionPolicy();
        double threshold = DatacenterInfo.getThreshold();
        VmAllocationPolicy vmAllocationPolicy = getVmAllocationPolicy(vmAllocationPolicyType, vmSelectionPolicyType, threshold, hostList);

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch,
                os,
                vmm,
                hostList,
                time_zone,
                cost,
                costPerMem,
                costPerStorage,
                costPerBw);

        // 6. Finally, we need to create a NetworkDatacenter object.
        PowerDatacenter datacenter = null;
        try {

            datacenter = new PowerDatacenter(
                    DatacenterInfo.getName(),
                    characteristics,
                    vmAllocationPolicy,
                    storageList,
                    300.0);//zax: to do schedulingInterval is the delay to perform som action, i need to check it.
            datacenter.setDisableMigrations(false);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return datacenter;
    }

    private NetworkDatacenter createNetworkDatacenter(VMDNodeWidget nodeWidget) {
        List<Node> nodeWidgetsConnected = getNodesConnectedTo(nodeWidget);
        List<NetworkHost> hostList = new ArrayList<NetworkHost>();
        System.err.println("nodeWidgetsConnected size = " + nodeWidgetsConnected.size());
        for (Node node : nodeWidgetsConnected) {
            System.err.println("node = " + node);
            if (node instanceof HostNode) {
                System.err.println("host node = " + node);
                hostList.addAll(createNetworkHost((HostNode) node, hostList.size()));
            } else {
                System.err.println("Error: Can not connect " + node.getDisplayName() + "to the " + nodeWidget.getNodeName());
            }
        }

        DatacenterAPI DatacenterInfo = ((DatacenterNode) scene.getNode(nodeWidget)).getObj();
        String arch = DatacenterInfo.getArch(); // system architecture
        String os = DatacenterInfo.getOs(); // operating system
        String vmm = DatacenterInfo.getVmm();
        double time_zone = 10.0;//DatacenterInfo.getTime(); // time zone this resource located
        double cost = DatacenterInfo.getCostPerSecond(); // the cost of using processing in this resource
        double costPerMem = DatacenterInfo.getCostPerMemory(); // the cost of using memory in this resource
        double costPerStorage = DatacenterInfo.getCostPerStorage(); // the cost of using storage in this resource
        double costPerBw = DatacenterInfo.getCostPerBandwidth(); // the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch,
                os,
                vmm,
                hostList,
                time_zone,
                cost,
                costPerMem,
                costPerStorage,
                costPerBw);

        // 6. Finally, we need to create a NetworkDatacenter object.
        NetworkDatacenter datacenter = null;
        try {
            System.err.println("DatacenterInfo.getName() = " + DatacenterInfo.getName());
            System.err.println("characteristics = " + characteristics);
            System.err.println("hostList = " + hostList);
            System.err.println("hostList.size() = " + hostList.size());
            datacenter = new NetworkDatacenter(
                    DatacenterInfo.getName(),
                    characteristics,
                    new NetworkVmAllocationPolicy(hostList), //zax: to do select from list
                    storageList,
                    0);//zax: to do schedulingInterval is the delay to perform som action, i need to check it.

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("datacenter = " + datacenter);
        // Create Internal Datacenter network
        CreateNetwork(datacenter, DatacenterInfo);


        return datacenter;
    }

    private List<Host> createHost(HostNode node, int startHostID) {

        List<Host> hosts = new ArrayList<Host>();

        HostAPI hostInfo = node.getObj();
        int mips = hostInfo.getMips();
        int ram = hostInfo.getRam(); // host memory (MB)
        long storage = hostInfo.getStorage(); // host storage
        int bw = hostInfo.getBw();
        List<Pe> peList = new ArrayList<Pe>();
        for (int i = 0; i < hostInfo.getCpuNo(); i++) {
            peList.add(new Pe(i, getPeProvisioner(hostInfo.getPeProvisioner(), mips)));
        }
        for (int i = 0; i < hostInfo.getHostNo(); i++) {
            hosts.add(
                    new Host(
                    startHostID + i,
                    getRamProvisioner(hostInfo.getRamProvisioner(), ram),
                    getBwProvisioner(hostInfo.getBwProvisioner(), bw),
                    storage,
                    peList,
                    getVmScheduler(hostInfo.getVmSchedular(), peList)));
        }


        return hosts;
    }

    private List<NetworkHost> createNetworkHost(HostNode node, int startHostID) {

        List<NetworkHost> networkHosts = new ArrayList<NetworkHost>();

        HostAPI hostInfo = node.getObj();
        int mips = hostInfo.getMips();
        int ram = hostInfo.getRam(); // host memory (MB)
        long storage = hostInfo.getStorage(); // host storage
        int bw = hostInfo.getBw();
        List<Pe> peList = new ArrayList<Pe>();
        for (int i = 0; i < hostInfo.getCpuNo(); i++) {
            peList.add(new Pe(i, getPeProvisioner(hostInfo.getPeProvisioner(), mips)));
        }
        for (int i = 0; i < hostInfo.getHostNo(); i++) {
            networkHosts.add(
                    new NetworkHost(
                    startHostID + i,
                    getRamProvisioner(hostInfo.getRamProvisioner(), ram),
                    getBwProvisioner(hostInfo.getBwProvisioner(), bw),
                    storage,
                    peList,
                    getVmScheduler(hostInfo.getVmSchedular(), peList)));
        }


        return networkHosts;
    }

    private List<PowerHost> createPowerHost(HostNode node, int startHostID) {

        List<PowerHost> hosts = new ArrayList<PowerHost>();

        HostAPI hostInfo = node.getObj();
        int mips = hostInfo.getMips();
        int ram = hostInfo.getRam(); // host memory (MB)
        long storage = hostInfo.getStorage(); // host storage
        int bw = hostInfo.getBw();
        PowerModel hostPower = getPowerModel(hostInfo.getHostPower());
        List<Pe> peList = new ArrayList<Pe>();
        for (int i = 0; i < hostInfo.getCpuNo(); i++) {
            peList.add(new Pe(i, getPeProvisioner(hostInfo.getPeProvisioner(), mips)));
        }
        for (int i = 0; i < hostInfo.getHostNo(); i++) {
            hosts.add(
                    new PowerHostUtilizationHistory(
                    startHostID + i,
                    getRamProvisioner(hostInfo.getRamProvisioner(), ram),
                    getBwProvisioner(hostInfo.getBwProvisioner(), bw),
                    storage,
                    peList,
                    //zax
                    new VmSchedulerTimeSharedOverSubscription(peList),  
                    new PowerModelSpecPowerHpProLiantMl110G4Xeon3040()
                    //zax    
                    //getVmScheduler(hostInfo.getVmSchedular(), peList),
                    //hostPower
                            ));
        }


        return hosts;
    }

    private PowerModel getPowerModel(String args) {
        //zax: to do parse args to array
        final double[] power = {86, 89.4, 92.6, 96, 99.5, 102, 106, 108, 112, 114, 117};
        return new PowerModelSpecPower() {

            @Override
            protected double getPowerData(int index) {
                return power[index];
            }
        };
    }

    private void CreateNetwork(NetworkDatacenter dc, DatacenterAPI datacenterInfo) {

        int rootSwitchBandwidth = 20 * 1024 * 1024 * 2;//datacenterInfo.getRootSwitchBandwidth();//zax
        double rootSwitchDelay = 0.00285;//datacenterInfo.getRootSwitchDelay();//zax
        int rootSwitchPortsNo = 8;

        int aggregateSwitchBandwidth = 100 * 1024 * 1024;//datacenterInfo.getAggregateSwitchBandwidth();//zax
        double aggregateSwitchDelay = 0.00245;//datacenterInfo.getAggregateSwitchDelay();//zax
        int aggregateSwitchPortsNo = 8;

        int edgeSwitchBandwidth = 100 * 1024 * 1024;//datacenterInfo.getEdgeSwitchBandwidth();//zax 
        double edgeSwitchDelay = 0.00157;//datacenterInfo.getEdgeSwitchDelay();//zax
        int edgeSwitchPortsNo = 8;

        System.err.println("dc.getHostList().size() = " + dc.getHostList().size());
        int edgeSwitchNo = (int) Math.ceil((double) dc.getHostList().size() / (double) edgeSwitchPortsNo);
        int aggregateSwitchNo = (int) Math.ceil((double) edgeSwitchNo / (double) aggregateSwitchPortsNo);
        int rootSwitchNo = (int) Math.ceil((double) aggregateSwitchNo / (double) rootSwitchPortsNo);
        
        System.err.println("edgeSwitchNo = "+edgeSwitchNo);
        System.err.println("aggregateSwitchNo = "+aggregateSwitchNo);
        System.err.println("rootSwitchNo = "+rootSwitchNo);

        RootSwitch rootSwitch[] = new RootSwitch[rootSwitchNo];
        for (int i = 0; i < rootSwitch.length; i++) {
            rootSwitch[i] = new RootSwitch("Root" + i, NetworkConstants.ROOT_LEVEL, dc);
            rootSwitch[i].numport = rootSwitchPortsNo;
            rootSwitch[i].uplinkbandwidth = rootSwitchBandwidth;
            rootSwitch[i].downlinkbandwidth = rootSwitchBandwidth;
            rootSwitch[i].latency = rootSwitchDelay;
            rootSwitch[i].switching_delay = rootSwitchDelay;
        }

        AggregateSwitch aggregateSwitch[] = new AggregateSwitch[aggregateSwitchNo];
        for (int i = 0; i < aggregateSwitch.length; i++) {
            aggregateSwitch[i] = new AggregateSwitch("Aggregate" + i, NetworkConstants.Agg_LEVEL, dc);
            aggregateSwitch[i].numport = aggregateSwitchPortsNo;
            aggregateSwitch[i].uplinkbandwidth = rootSwitchBandwidth;
            aggregateSwitch[i].downlinkbandwidth = aggregateSwitchBandwidth;
            aggregateSwitch[i].latency = aggregateSwitchDelay;
            aggregateSwitch[i].switching_delay = aggregateSwitchDelay;

        }

        AggregateSwitch aggregateSwitchL2[] = new AggregateSwitch[aggregateSwitchNo];
        for (int i = 0; i < aggregateSwitchL2.length; i++) {
            aggregateSwitchL2[i] = new AggregateSwitch("Aggregate" + i, NetworkConstants.Agg_LEVEL, dc);
            aggregateSwitchL2[i].numport = aggregateSwitchPortsNo;
            aggregateSwitchL2[i].uplinkbandwidth = aggregateSwitchBandwidth;
            aggregateSwitchL2[i].downlinkbandwidth = aggregateSwitchBandwidth;
            aggregateSwitchL2[i].latency = aggregateSwitchDelay;
            aggregateSwitchL2[i].switching_delay = aggregateSwitchDelay;

        }

        EdgeSwitch edgeswitch[] = new EdgeSwitch[edgeSwitchNo];
        for (int i = 0; i < edgeswitch.length; i++) {
            edgeswitch[i] = new EdgeSwitch("Edge" + i, NetworkConstants.EDGE_LEVEL, dc);
            edgeswitch[i].numport = edgeSwitchPortsNo;
            edgeswitch[i].uplinkbandwidth = aggregateSwitchBandwidth;
            edgeswitch[i].downlinkbandwidth = edgeSwitchBandwidth;
            edgeswitch[i].latency = edgeSwitchDelay;
            edgeswitch[i].switching_delay = edgeSwitchDelay;
        }
        //<editor-fold desc="VL2">
        if (datacenterInfo.getNetworkTopology().equals("VL2")) {

            for (int edgeID = 0, aggregateSwitchID = 0, rootSwitchID = 0; edgeID < edgeSwitchNo; edgeID++, aggregateSwitchID += (edgeID % edgeSwitchPortsNo == 0 ? 1 : 0), rootSwitchID += (edgeID % (aggregateSwitchPortsNo*edgeSwitchPortsNo) == 0 ? 1 : 0)) {
                
                System.err.println("edgeID = "+edgeID);
                System.err.println("aggregateSwitchID = "+aggregateSwitchID);
                System.err.println("rootSwitchID = "+rootSwitchID);
                
                edgeswitch[edgeID].uplinkswitches.add(aggregateSwitch[aggregateSwitchID]);
                aggregateSwitch[aggregateSwitchID].uplinkswitches.add(aggregateSwitchL2[aggregateSwitchID]);
                aggregateSwitchL2[aggregateSwitchID].uplinkswitches.add(rootSwitch[rootSwitchID]);


                rootSwitch[rootSwitchID].downlinkswitches.add(aggregateSwitchL2[aggregateSwitchID]);
                aggregateSwitchL2[aggregateSwitchID].downlinkswitches.add(aggregateSwitch[aggregateSwitchID]);
                aggregateSwitch[aggregateSwitchID].downlinkswitches.add(edgeswitch[edgeID]);

                dc.Switchlist.put(edgeswitch[edgeID].getId(), edgeswitch[edgeID]);
                dc.Switchlist.put(aggregateSwitch[aggregateSwitchID].getId(), aggregateSwitch[aggregateSwitchID]);
                dc.Switchlist.put(rootSwitch[rootSwitchID].getId(), rootSwitch[rootSwitchID]);

            }

            for (Host hs : dc.getHostList()) {
                NetworkHost hs1 = (NetworkHost) hs;
                hs1.bandwidth = edgeSwitchBandwidth;
                int switchnum = (int) (hs.getId() / edgeSwitchPortsNo);
                edgeswitch[switchnum].hostlist.put(hs.getId(), hs1);
                dc.HostToSwitchid.put(hs.getId(), edgeswitch[switchnum].getId());
                hs1.sw = edgeswitch[switchnum];
                List<NetworkHost> hslist = hs1.sw.fintimelistHost.get(0D);
                if (hslist == null) {
                    hslist = new ArrayList<NetworkHost>();
                    hs1.sw.fintimelistHost.put(0D, hslist);
                }
                hslist.add(hs1);

            }
            //</editor-fold>
            //<editor-fold desc="BCube">
        } else if (datacenterInfo.getNetworkTopology().equals("BCube")) {
            for (int edgeID = 0, aggregateSwitchID = 0, rootSwitchID = 0; edgeID < edgeSwitchNo; edgeID++, aggregateSwitchID += (edgeID % edgeSwitchPortsNo == 0 ? 1 : 0), rootSwitchID += (edgeID % (aggregateSwitchPortsNo*edgeSwitchPortsNo) == 0 ? 1 : 0)) {
                edgeswitch[edgeID].uplinkswitches.add(aggregateSwitch[aggregateSwitchID]);
                aggregateSwitch[aggregateSwitchID].uplinkswitches.add(rootSwitch[rootSwitchID]);


                rootSwitch[rootSwitchID].downlinkswitches.add(aggregateSwitch[aggregateSwitchID]);
                aggregateSwitch[aggregateSwitchID].downlinkswitches.add(edgeswitch[edgeID]);

                dc.Switchlist.put(edgeswitch[edgeID].getId(), edgeswitch[edgeID]);
                dc.Switchlist.put(aggregateSwitch[aggregateSwitchID].getId(), aggregateSwitch[aggregateSwitchID]);
                dc.Switchlist.put(rootSwitch[rootSwitchID].getId(), rootSwitch[rootSwitchID]);

            }

            for (Host hs : dc.getHostList()) {
                NetworkHost hs1 = (NetworkHost) hs;
                hs1.bandwidth = edgeSwitchBandwidth;
                int switchnum = (int) (hs.getId() / edgeSwitchPortsNo);
                edgeswitch[switchnum].hostlist.put(hs.getId(), hs1);
                dc.HostToSwitchid.put(hs.getId(), edgeswitch[switchnum].getId());
                hs1.sw = edgeswitch[switchnum];
                List<NetworkHost> hslist = hs1.sw.fintimelistHost.get(0D);
                if (hslist == null) {
                    hslist = new ArrayList<NetworkHost>();
                    hs1.sw.fintimelistHost.put(0D, hslist);
                }
                hslist.add(hs1);

            }

            //</editor-fold>
            //<editor-fold desc="PortLand">
        } else if (datacenterInfo.getNetworkTopology().equals("PortLand")) {

            for (int edgeID = 0, aggregateSwitchID = 0, rootSwitchID = 0; edgeID < edgeSwitchNo; edgeID++, aggregateSwitchID += (edgeID % edgeSwitchPortsNo == 0 ? 1 : 0), rootSwitchID += (edgeID % (aggregateSwitchPortsNo*edgeSwitchPortsNo) == 0 ? 1 : 0)) {

                edgeswitch[edgeID].uplinkswitches.add(aggregateSwitch[aggregateSwitchID]);
                aggregateSwitch[aggregateSwitchID].uplinkswitches.add(rootSwitch[rootSwitchID]);


                rootSwitch[rootSwitchID].downlinkswitches.add(aggregateSwitch[aggregateSwitchID]);
                aggregateSwitch[aggregateSwitchID].downlinkswitches.add(edgeswitch[edgeID]);

                dc.Switchlist.put(edgeswitch[edgeID].getId(), edgeswitch[edgeID]);
                dc.Switchlist.put(aggregateSwitch[aggregateSwitchID].getId(), aggregateSwitch[aggregateSwitchID]);
                dc.Switchlist.put(rootSwitch[rootSwitchID].getId(), rootSwitch[rootSwitchID]);

            }

            for (Host hs : dc.getHostList()) {
                NetworkHost hs1 = (NetworkHost) hs;
                hs1.bandwidth = edgeSwitchBandwidth;
                int switchnum = (int) (hs.getId() / edgeSwitchPortsNo);
                edgeswitch[switchnum].hostlist.put(hs.getId(), hs1);
                dc.HostToSwitchid.put(hs.getId(), edgeswitch[switchnum].getId());
                hs1.sw = edgeswitch[switchnum];
                List<NetworkHost> hslist = hs1.sw.fintimelistHost.get(0D);
                if (hslist == null) {
                    hslist = new ArrayList<NetworkHost>();
                    hs1.sw.fintimelistHost.put(0D, hslist);
                }
                hslist.add(hs1);
            }

            //</editor-fold>
            //<editor-fold desc="DCell">
        } else if (datacenterInfo.getNetworkTopology().equals("DCell")) {

            for (int edgeID = 0, aggregateSwitchID = 0, rootSwitchID = 0; edgeID < edgeSwitchNo; edgeID++, aggregateSwitchID += (edgeID % edgeSwitchPortsNo == 0 ? 1 : 0), rootSwitchID += (edgeID % (aggregateSwitchPortsNo*edgeSwitchPortsNo) == 0 ? 1 : 0)) {

                edgeswitch[edgeID].uplinkswitches.add(aggregateSwitch[aggregateSwitchID]);
                aggregateSwitch[aggregateSwitchID].uplinkswitches.add(rootSwitch[rootSwitchID]);


                rootSwitch[rootSwitchID].downlinkswitches.add(aggregateSwitch[aggregateSwitchID]);
                aggregateSwitch[aggregateSwitchID].downlinkswitches.add(edgeswitch[edgeID]);

                dc.Switchlist.put(edgeswitch[edgeID].getId(), edgeswitch[edgeID]);
                dc.Switchlist.put(aggregateSwitch[aggregateSwitchID].getId(), aggregateSwitch[aggregateSwitchID]);
                dc.Switchlist.put(rootSwitch[rootSwitchID].getId(), rootSwitch[rootSwitchID]);

            }

            for (Host hs : dc.getHostList()) {
                NetworkHost hs1 = (NetworkHost) hs;
                hs1.bandwidth = edgeSwitchBandwidth;
                int switchnum = (int) (hs.getId() / edgeSwitchPortsNo);
                edgeswitch[switchnum].hostlist.put(hs.getId(), hs1);
                dc.HostToSwitchid.put(hs.getId(), edgeswitch[switchnum].getId());
                hs1.sw = edgeswitch[switchnum];
                List<NetworkHost> hslist = hs1.sw.fintimelistHost.get(0D);
                if (hslist == null) {
                    hslist = new ArrayList<NetworkHost>();
                    hs1.sw.fintimelistHost.put(0D, hslist);
                }
                hslist.add(hs1);
            }
        }
        //</editor-fold>

    }

    private DatacenterBroker createDatacenterBroker(VMDNodeWidget nodeWidget) {
        BrokerAPI brokerInfo = ((BrokerNode) scene.getNode(nodeWidget)).getObj();
        DatacenterBroker broker = null;
        try {
            broker = new DatacenterBroker(brokerInfo.getName());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        List<Node> nodeWidgetsConnected = getNodesConnectedTo(nodeWidget);
        Node n0 = nodeWidgetsConnected.get(0);
        Node n1 = nodeWidgetsConnected.get(1);
        List<Vm> vmList = new ArrayList<Vm>();
        List<Cloudlet> cloudletList = new ArrayList<Cloudlet>();

        for (Node node : nodeWidgetsConnected) {
            if (node instanceof VMNode) {
                vmList.addAll(createVM((VMNode) node, vmList.size(), broker.getId()));
            } else if (node instanceof CloudletNode) {
                cloudletList.addAll(createCloudlet((CloudletNode) node, cloudletList.size(), broker.getId()));
            } else if (node instanceof DatacenterNode) {
            } else {
                System.err.println("Error: Can not connect " + node.getDisplayName() + "to the " + nodeWidget.getNodeName());
            }
        }

        broker.submitVmList(vmList);
        broker.submitCloudletList(cloudletList);

        return broker;
    }

    List<NetworkVm> _vmList = new ArrayList<NetworkVm>();
    private NetworkDatacenterBroker createNetworkDatacenterBroker(VMDNodeWidget nodeWidget) {
        BrokerAPI brokerInfo = ((BrokerNode) scene.getNode(nodeWidget)).getObj();
        NetworkDatacenterBroker broker = null;
        try {
            broker = new NetworkDatacenterBroker(brokerInfo.getName());
            broker.setInterpreter(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        List<Node> nodeWidgetsConnected = getNodesConnectedTo(nodeWidget);

        List<NetworkVm> vmList = new ArrayList<NetworkVm>();
        List<NetworkCloudlet> cloudletList = new ArrayList<NetworkCloudlet>();
        NetworkDatacenter linkDC = null;

        for (Node node : nodeWidgetsConnected) {
            
            if (node instanceof VMNode) {
                vmList.addAll(createNetworkVM((VMNode) node, vmList.size(), broker.getId()));
            } 
        }
        _vmList.addAll(vmList);
        broker.submitVmList(vmList);
        
        for (Node node : nodeWidgetsConnected) {
            
            if (node instanceof CloudletNode) {
                cloudletList.addAll(createNetworkCloudlet((CloudletNode) node, cloudletList.size(), broker.getId()));
            } else if (node instanceof DatacenterNode) {
                linkDC = mapNodeNetworkDatacenter.get(node);
            } else if(!(node instanceof VMNode || node instanceof CloudletNode || node instanceof DatacenterNode)){
                System.err.println("Error: Can not connect " + node.getDisplayName() + "to the " + nodeWidget.getNodeName());
            }
        }

        
        broker.submitCloudletList(cloudletList);
        broker.setLinkDC(linkDC);
        return broker;
    }

    private PowerDatacenterBroker createPowerDatacenterBroker(VMDNodeWidget nodeWidget) {
        BrokerAPI brokerInfo = ((BrokerNode) scene.getNode(nodeWidget)).getObj();
        PowerDatacenterBroker broker = null;
        try {
            broker = new PowerDatacenterBroker(brokerInfo.getName());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        List<Node> nodeWidgetsConnected = getNodesConnectedTo(nodeWidget);

        List<PowerVm> vmList = new ArrayList<PowerVm>();
        List<Cloudlet> cloudletList = new ArrayList<Cloudlet>();
        NetworkDatacenter linkDC = null;

        for (Node node : nodeWidgetsConnected) {
            if (node instanceof VMNode) {
                vmList.addAll(createPowerVM((VMNode) node, vmList.size(), broker.getId()));
            } else if (node instanceof CloudletNode) {
                cloudletList.addAll(createCloudlet((CloudletNode) node, cloudletList.size(), broker.getId()));
            } else if (node instanceof DatacenterNode) {
//                linkDC = mapNodeNetworkDatacenter.get(node);
            } else {
                System.err.println("Error: Can not connect " + node.getDisplayName() + "to the " + nodeWidget.getNodeName());
            }
        }

        broker.submitVmList(vmList);
        broker.submitCloudletList(cloudletList);

        return broker;
    }

    private List<Vm> createVM(VMNode node, int startVMID, int userID) {
        List<Vm> vms = new ArrayList<Vm>();

        VMAPI vmInfo = node.getObj();
        int mips = vmInfo.getMips();
        long size = vmInfo.getSize();
        int ram = vmInfo.getRam(); // host memory (MB)
        int bw = vmInfo.getBw();
        int pesNumber = vmInfo.getCpuNo(); //number of cpus
        String vmm = vmInfo.getVmm(); //VMM name

        for (int i = 0; i < vmInfo.getVMNo(); i++) {
            vms.add(new Vm(startVMID + i, userID, mips, pesNumber, ram, bw, size, vmm, getCloudletScheduler(vmInfo.getTaskSchedular(), NORMAL, mips, pesNumber)));
        }
        this.vms.addAll(vms);

        return vms;
    }

    private List<NetworkVm> createNetworkVM(VMNode node, int startVMID, int userID) {
        List<NetworkVm> networkVms = new ArrayList<NetworkVm>();

        VMAPI vmInfo = node.getObj();
        int mips = vmInfo.getMips();
        long size = vmInfo.getSize();
        int ram = vmInfo.getRam(); // host memory (MB)
        int bw = vmInfo.getBw();
        int pesNumber = vmInfo.getCpuNo(); //number of cpus
        String vmm = vmInfo.getVmm(); //VMM name

        for (int i = 0; i < vmInfo.getVMNo(); i++) {
            networkVms.add(new NetworkVm(startVMID + i, userID, mips, pesNumber, ram, bw, size, vmm, getCloudletScheduler(vmInfo.getTaskSchedular(), NETWORK, mips, pesNumber)));
        }
        this.vms.addAll(vms);

        return networkVms;
    }

    private List<PowerVm> createPowerVM(VMNode node, int startVMID, int userID) {
        List<PowerVm> vms = new ArrayList<PowerVm>();

        VMAPI vmInfo = node.getObj();
        int mips = vmInfo.getMips();
        long size = vmInfo.getSize();
        int ram = vmInfo.getRam(); // host memory (MB)
        int bw = vmInfo.getBw();
        int pesNumber = vmInfo.getCpuNo(); //number of cpus
        String vmm = vmInfo.getVmm(); //VMM name
        int priority = vmInfo.getPriority();
        double schedulingInterval = vmInfo.getSchedulingInterval();

        for (int i = 0; i < vmInfo.getVMNo(); i++) {
            vms.add(new PowerVm(
                    startVMID + i,
                    userID,
                    mips,
                    pesNumber,
                    ram,
                    bw,
                    size,
                    priority,
                    vmm,
                    //zax
                    new CloudletSchedulerDynamicWorkload(mips, pesNumber),
                    //zax
                    //getCloudletScheduler(vmInfo.getTaskSchedular(), POWER, mips, pesNumber),
                    schedulingInterval));
        }
        this.vms.addAll(vms);

        return vms;
    }

    private List<Cloudlet> createCloudlet(CloudletNode node, int startcloudletID, int userID) {
        List<Cloudlet> cloudlets = new ArrayList<Cloudlet>();

        CloudletAPI cloudletInfo = node.getObj();

        int pesNumber = cloudletInfo.getCpuNo();
        long length = cloudletInfo.getCloudletLength();
        long fileSize = cloudletInfo.getCloudletFileSize();
        long outputSize = cloudletInfo.getCloudletOutputSize();
        int memory = cloudletInfo.getMemory();

        if ("Cloudlet".equals(cloudletInfo.getType())) {
            for (int i = 0; i < cloudletInfo.getCloudletNo(); i++) {
//                Cloudlet c = new Cloudlet(
//                        startcloudletID + i,
//                        length,
//                        pesNumber,
//                        fileSize,
//                        outputSize,
//                        getCPUUtilizationModel(cloudletInfo.getUtilizationModelCpu()),
//                        getRamUtilizationModel(cloudletInfo.getUtilizationModelRam()),
//                        getBWUtilizationModel(cloudletInfo.getUtilizationModelBw()));
                
                UtilizationModel utilizationModelNull = new UtilizationModelNull();
                Cloudlet c = new Cloudlet(
						i,
						length,
						pesNumber,
						fileSize,
						outputSize,
						new UtilizationModelStochastic(i),//zax
						utilizationModelNull,//zax 
						utilizationModelNull);// zax
//                c.setVmId(0);
                //zax
                
                c.setUserId(userID);
//                c.setVmId(i % this.vms.size());//zax try to comment it (put to simulate power)
                cloudlets.add(c);
            }
        } else if ("Mapper".equals(cloudletInfo.getType())) {
            for (int i = 0; i < cloudletInfo.getCloudletNo(); i++) {
                jo.just.mapreduce.Map m = new jo.just.mapreduce.Map(
                        startcloudletID + i,
                        length,
                        pesNumber,
                        fileSize,
                        outputSize,
                        getCPUUtilizationModel(cloudletInfo.getUtilizationModelCpu()),
                        getRamUtilizationModel(cloudletInfo.getUtilizationModelRam()),
                        getBWUtilizationModel(cloudletInfo.getUtilizationModelBw()));
                m.setUserId(userID);
                Master.getInstance().AddMaper(m);
                cloudlets.add(m);
            }
        } else if ("Reducer".equals(cloudletInfo.getType())) {
            for (int i = 0; i < cloudletInfo.getCloudletNo(); i++) {
                Reduce r = new Reduce(
                        startcloudletID + i,
                        length,
                        pesNumber,
                        fileSize,
                        outputSize,
                        getCPUUtilizationModel(cloudletInfo.getUtilizationModelCpu()),
                        getRamUtilizationModel(cloudletInfo.getUtilizationModelRam()),
                        getBWUtilizationModel(cloudletInfo.getUtilizationModelBw()));
                r.setUserId(userID);
                Master.getInstance().AddReduce(r);
                cloudlets.add(r);
            }

        } else if ("Rain".equals(cloudletInfo.getType())) {
            RainOlioWorkload rainOlioWorkload = new RainOlioWorkload("/Users/apple/Desktop/cloudsim/TeachCloud/CloudSim/src/jo/just/cloudsim/rain.config.olio.json", 1);
            cloudlets = rainOlioWorkload.generateWorkload().subList(0, cloudletInfo.getCloudletNo());

        } else if ("MapReduce".equals(cloudletInfo.getType())) {
        } else if ("SWF".equals(cloudletInfo.getType())) {
            WorkloadFileReader workloadFileReader = null;
            try {
                workloadFileReader = new WorkloadFileReader("/Users/apple/Desktop/cloudsim/TeachCloud/CloudSim/src/jo/just/cloudsim/LCG.swf.gz", 1);
            } catch (FileNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            }
            cloudlets = workloadFileReader.generateWorkload().subList(0, cloudletInfo.getCloudletNo());
        }

        return cloudlets;
    }

    private List<NetworkCloudlet> createNetworkCloudlet(CloudletNode node, int startcloudletID, int userID) {
        List<NetworkCloudlet> networkCloudlets = new ArrayList<NetworkCloudlet>();

        CloudletAPI cloudletInfo = node.getObj();
        if ("Cloudlet".equals(cloudletInfo.getType())) {

            int pesNumber = cloudletInfo.getCpuNo();
            long length = cloudletInfo.getCloudletLength();
            long fileSize = cloudletInfo.getCloudletFileSize();
            long outputSize = cloudletInfo.getCloudletOutputSize();
            int memory = cloudletInfo.getMemory();

            for (int i = 0; i < cloudletInfo.getCloudletNo(); i++) {
               
                NetworkCloudlet nc = new NetworkCloudlet(
                        startcloudletID + i,
                        length,
                        pesNumber,
                        fileSize,
                        outputSize,
                        memory,
                        getCPUUtilizationModel(cloudletInfo.getUtilizationModelCpu()),
                        getRamUtilizationModel(cloudletInfo.getUtilizationModelRam()),
                        getBWUtilizationModel(cloudletInfo.getUtilizationModelBw()));
                nc.numStage = 2;//zax to do chick it
                nc.setUserId(userID);
                nc.submittime = CloudSim.clock();
		nc.currStagenum = -1;
                
                int vmID = _vmList.get(i % _vmList.size()).getId();//zax to do chick it from org.cloudbus.cloudsim.network.datacenter.WorkflowApp
                nc.setVmId(vmID);
                
                nc.stages.add(
                        new TaskStage(
                                NetworkConstants.EXECUTION,
                                0,
                                1000 * 0.8,
                                0,
                                memory,
                                vmID,
                                nc.getCloudletId()));
                
                networkCloudlets.add(nc);
            }
        } else if ("Mapper".equals(cloudletInfo.getType())) {
        } else if ("Reducer".equals(cloudletInfo.getType())) {
        } else if ("Rain".equals(cloudletInfo.getType())) {
        } else if ("MapReduce".equals(cloudletInfo.getType())) {
        } else if ("SWF".equals(cloudletInfo.getType())) {
            NetworkWorkloadFileReader workloadFileReader = null;
            try {
                workloadFileReader = new NetworkWorkloadFileReader(cloudletInfo.getPath(), 1);
            } catch (FileNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            }
            networkCloudlets = workloadFileReader.generateNetworkWorkload().subList(0, cloudletInfo.getCloudletNo());
        } else if ("Random".equals(cloudletInfo.getType())) {
        }

        return networkCloudlets;
    }

    private List<Node> getNodesConnectedTo(VMDNodeWidget nodeWidget) {

        List<Node> nodeWidgetsConnected = new ArrayList<Node>();
        List<VMDPinWidget> pins = getPinWidgetTo(nodeWidget);


        String node = scene.findObject(nodeWidget).toString();

        for (ConnectionWidget edge : connectionWidget) {
            String srcpin = scene.getEdgeSource(scene.findObject(edge).toString());
            String targetpin = scene.getEdgeTarget(scene.findObject(edge).toString());

            String srcNode = scene.getPinNode(srcpin);
            String targetNode = scene.getPinNode(targetpin);

            if (srcNode.equals(node)) {
                VMDNodeWidget nw = getNodeWidget(targetNode);
                if (nw != null) {
                    nodeWidgetsConnected.add(scene.getNode(nw));
                }
            } else if (targetNode.equals(node)) {
                VMDNodeWidget nw = getNodeWidget(srcNode);
                if (nw != null) {
                    nodeWidgetsConnected.add(scene.getNode(nw));
                }
            }

        }

        return nodeWidgetsConnected;
    }

    private List<VMDPinWidget> getPinWidgetTo(VMDNodeWidget nodeWidget) {
        List<VMDPinWidget> pins = new ArrayList<VMDPinWidget>();
        for (Widget w : nodeWidget.getChildren()) {
            if (w instanceof VMDPinWidget) {
                pins.add((VMDPinWidget) w);
            }

        }
        return pins;
    }

    private VMDNodeWidget getNodeWidget(String name) {

        for (VMDNodeWidget nw : nodeWidgets) {
            if (scene.findObject(nw).toString().equals(name)) {
                return nw;
            }
        }
        return null;
    }

    private PeProvisioner getPeProvisioner(String name, int mips) {
        if ("Simple".equals(name)) {
            return new PeProvisionerSimple(mips);
        } else {
            return new PeProvisionerSimple(mips);
        }
    }

    private RamProvisioner getRamProvisioner(String name, int ram) {
        if ("Simple".equals(name)) {
            return new RamProvisionerSimple(ram);
        } else {
            return new RamProvisionerSimple(ram);
        }
    }

    private BwProvisioner getBwProvisioner(String name, int bw) {
        if ("Simple".equals(name)) {
            return new BwProvisionerSimple(bw);
        } else {
            return new BwProvisionerSimple(bw);
        }
    }

    private VmScheduler getVmScheduler(String name, List<Pe> peList) {
        if ("Time Shared".equals(name)) {
            return new VmSchedulerTimeShared(peList);
        } else if ("Space Shared".equals(name)) {
            return new VmSchedulerSpaceShared(peList);
        } else if ("Time Shared Over Subscription".equals(name)) {
            return new VmSchedulerTimeSharedOverSubscription(peList);
        } else {
            return new VmSchedulerTimeShared(peList);
        }
    }

    private CloudletScheduler getCloudletScheduler(String name, int type, int mips, int pesNo) {
        if ("Time Shared".equals(name)) {
            switch (type) {
                case NORMAL:
                    return new CloudletSchedulerTimeShared();
                case NETWORK:
                    System.err.println("Error: NetworkCloudletTimeSharedScheduler not supported yet");
                    return new NetworkCloudletSpaceSharedScheduler();
                case POWER:
                    return new CloudletSchedulerTimeShared();
            }

        } else if ("Space Shared".equals(name)) {
            switch (type) {
                case NORMAL:
                    return new CloudletSchedulerSpaceShared();
                case NETWORK:
                    return new NetworkCloudletSpaceSharedScheduler();
                case POWER:
                    return new CloudletSchedulerSpaceShared();
            }

        } else if ("Dynamic Workload".equals(name)) {
            return new CloudletSchedulerDynamicWorkload(mips, pesNo);
        } else if ("Dynamic Workload".equals(name)) {
            return new MapReduceSchedulerSpaceShared();
        }

        return null;
    }

    private UtilizationModel getCPUUtilizationModel(String name) {
        if ("UtilizationModelFull".equals(name)) {
            return new UtilizationModelFull();
        } else {
            return new UtilizationModelFull();
        }
    }

    private UtilizationModel getRamUtilizationModel(String name) {
        if ("UtilizationModelFull".equals(name)) {
            return new UtilizationModelFull();
        } else {
            return new UtilizationModelFull();
        }
    }

    private UtilizationModel getBWUtilizationModel(String name) {
        if ("UtilizationModelFull".equals(name)) {
            return new UtilizationModelFull();
        } else {
            return new UtilizationModelFull();
        }
    }

    /**
     * Gets the vm allocation policy.
     * 
     * @param vmAllocationPolicyName the vm allocation policy name
     * @param vmSelectionPolicyName the vm selection policy name
     * @param parameterName the parameter name
     * @return the vm allocation policy
     */
    protected VmAllocationPolicy getVmAllocationPolicy(
            String vmAllocationPolicyName,
            String vmSelectionPolicyName,
            double parameter,
            List<? extends Host> hostList) {
        VmAllocationPolicy vmAllocationPolicy = null;
        PowerVmSelectionPolicy vmSelectionPolicy = null;
        if (!vmSelectionPolicyName.isEmpty()) {
            vmSelectionPolicy = getVmSelectionPolicy(vmSelectionPolicyName);
        }

        if (vmAllocationPolicyName.equals("IQR")) {
            PowerVmAllocationPolicyMigrationAbstract fallbackVmSelectionPolicy = new PowerVmAllocationPolicyMigrationStaticThreshold(
                    hostList,
                    vmSelectionPolicy,
                    0.7);
            vmAllocationPolicy = new PowerVmAllocationPolicyMigrationInterQuartileRange(
                    hostList,
                    vmSelectionPolicy,
                    parameter,
                    fallbackVmSelectionPolicy);
        } else if (vmAllocationPolicyName.equals("MAD")) {
            PowerVmAllocationPolicyMigrationAbstract fallbackVmSelectionPolicy = new PowerVmAllocationPolicyMigrationStaticThreshold(
                    hostList,
                    vmSelectionPolicy,
                    0.7);
            vmAllocationPolicy = new PowerVmAllocationPolicyMigrationMedianAbsoluteDeviation(
                    hostList,
                    vmSelectionPolicy,
                    parameter,
                    fallbackVmSelectionPolicy);
        } else if (vmAllocationPolicyName.equals("LR")) {
            PowerVmAllocationPolicyMigrationAbstract fallbackVmSelectionPolicy = new PowerVmAllocationPolicyMigrationStaticThreshold(
                    hostList,
                    vmSelectionPolicy,
                    0.7);
            vmAllocationPolicy = new PowerVmAllocationPolicyMigrationLocalRegression(
                    hostList,
                    vmSelectionPolicy,
                    parameter,
                    Constants.SCHEDULING_INTERVAL,
                    fallbackVmSelectionPolicy);
        } else if (vmAllocationPolicyName.equals("LRR")) {
            PowerVmAllocationPolicyMigrationAbstract fallbackVmSelectionPolicy = new PowerVmAllocationPolicyMigrationStaticThreshold(
                    hostList,
                    vmSelectionPolicy,
                    0.7);
            vmAllocationPolicy = new PowerVmAllocationPolicyMigrationLocalRegressionRobust(
                    hostList,
                    vmSelectionPolicy,
                    parameter,
                    Constants.SCHEDULING_INTERVAL,
                    fallbackVmSelectionPolicy);
        } else if (vmAllocationPolicyName.equals("THR")) {
            vmAllocationPolicy = new PowerVmAllocationPolicyMigrationStaticThreshold(
                    hostList,
                    vmSelectionPolicy,
                    parameter);
        } else if (vmAllocationPolicyName.equals("DVFS")) {
            vmAllocationPolicy = new PowerVmAllocationPolicySimple(hostList);
        } else {
            vmAllocationPolicy = new PowerVmAllocationPolicySimple(hostList);
        }
        return vmAllocationPolicy;
    }

    /**
     * Gets the vm selection policy.
     * 
     * @param vmSelectionPolicyName the vm selection policy name
     * @return the vm selection policy
     */
    protected PowerVmSelectionPolicy getVmSelectionPolicy(String vmSelectionPolicyName) {
        PowerVmSelectionPolicy vmSelectionPolicy = null;
        if (vmSelectionPolicyName.equals("MC")) {
            vmSelectionPolicy = new PowerVmSelectionPolicyMaximumCorrelation(
                    new PowerVmSelectionPolicyMinimumMigrationTime());
        } else if (vmSelectionPolicyName.equals("MMT")) {
            vmSelectionPolicy = new PowerVmSelectionPolicyMinimumMigrationTime();
        } else if (vmSelectionPolicyName.equals("MU")) {
            vmSelectionPolicy = new PowerVmSelectionPolicyMinimumUtilization();
        } else if (vmSelectionPolicyName.equals("RS")) {
            vmSelectionPolicy = new PowerVmSelectionPolicyRandomSelection();
        } else {
            vmSelectionPolicy = null;
        }
        return vmSelectionPolicy;
    }

    private void printCloudletReceivedList(/*List<?> brokerList*/) throws IOException {


        for (DatacenterBroker datacenterBroker : (List<DatacenterBroker>) datacenterBrokers) {
            List<Cloudlet> reciveCloudletList = datacenterBroker.getCloudletReceivedList();
            printCloudletList(reciveCloudletList);
            System.out.println("numberofcloudlet " + reciveCloudletList.size() + " Cached "
                    + NetworkDatacenterBroker.cachedcloudlet + " Data transfered "
                    + NetworkConstants.totaldatatransfer);
        }


        for (NetworkDatacenterBroker netDatacenterBroker : (List<NetworkDatacenterBroker>) netDatacenterBrokers) {
            List<Cloudlet> reciveCloudletList = netDatacenterBroker.getCloudletReceivedList();
            printCloudletList(reciveCloudletList);
            System.out.println("numberofcloudlet " + reciveCloudletList.size() + " Cached "
                    + NetworkDatacenterBroker.cachedcloudlet + " Data transfered "
                    + NetworkConstants.totaldatatransfer);
        }


        for (PowerDatacenterBroker powerDatacenterBroker : (List<PowerDatacenterBroker>) powerDatacenterBrokers) {
            List<Cloudlet> reciveCloudletList = powerDatacenterBroker.getCloudletReceivedList();
            printCloudletList(reciveCloudletList);
            System.out.println("numberofcloudlet " + reciveCloudletList.size() + " Cached "
                    + NetworkDatacenterBroker.cachedcloudlet + " Data transfered "
                    + NetworkConstants.totaldatatransfer);
        }



    }

    private void printCloudletList(List<Cloudlet> list) throws IOException {
        int size = list.size();
        Cloudlet cloudlet;
        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent + "Data center ID" + indent + "VM ID"
                + indent + "Time" + indent + "Start Time" + indent + "Finish Time");

        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");
                Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + indent
                        + cloudlet.getVmId() + indent + indent + dft.format(cloudlet.getActualCPUTime())
                        + indent + indent + dft.format(cloudlet.getExecStartTime()) + indent + indent
                        + dft.format(cloudlet.getFinishTime()));
            }
        }

    }

    private void printDebts(/*List<NetworkDatacenter> ndcl*/) {


        for (Datacenter ndc : datacenters) {
            ndc.printDebts();
        }

        for (NetworkDatacenter ndc : networkDatacenters) {
            ndc.printDebts();
        }

        for (PowerDatacenter ndc : powerDatacenters) {
            ndc.printDebts();
        }
    }

    /**
     * Prints the results.
     * 
     * @param datacenter the datacenter
     * @param lastClock the last clock
     * @param experimentName the experiment name
     * @param outputInCsv the output in csv
     * @param outputFolder the output folder
     */
    public void printResults(
            PowerDatacenter datacenter,
            List<Vm> vms,
            double lastClock,
            String experimentName,
            boolean outputInCsv,
            String outputFolder) {
        Log.enable();
        List<Host> hosts = datacenter.getHostList();

        int numberOfHosts = hosts.size();
        int numberOfVms = vms.size();

        double totalSimulationTime = lastClock;
        double energy = datacenter.getPower() / (3600 * 1000);
        int numberOfMigrations = datacenter.getMigrationCount();

        Map<String, Double> slaMetrics = getSlaMetrics(vms);

        double slaOverall = slaMetrics.get("overall");
        double slaAverage = slaMetrics.get("average");
        double slaDegradationDueToMigration = slaMetrics.get("underallocated_migration");
        // double slaTimePerVmWithMigration = slaMetrics.get("sla_time_per_vm_with_migration");
        // double slaTimePerVmWithoutMigration =
        // slaMetrics.get("sla_time_per_vm_without_migration");
        // double slaTimePerHost = getSlaTimePerHost(hosts);
        double slaTimePerActiveHost = getSlaTimePerActiveHost(hosts);

        double sla = slaTimePerActiveHost * slaDegradationDueToMigration;

        List<Double> timeBeforeHostShutdown = getTimesBeforeHostShutdown(hosts);

        int numberOfHostShutdowns = timeBeforeHostShutdown.size();

        double meanTimeBeforeHostShutdown = Double.NaN;
        double stDevTimeBeforeHostShutdown = Double.NaN;
        if (!timeBeforeHostShutdown.isEmpty()) {
            meanTimeBeforeHostShutdown = MathUtil.mean(timeBeforeHostShutdown);
            stDevTimeBeforeHostShutdown = MathUtil.stDev(timeBeforeHostShutdown);
        }

        List<Double> timeBeforeVmMigration = getTimesBeforeVmMigration(vms);
        double meanTimeBeforeVmMigration = Double.NaN;
        double stDevTimeBeforeVmMigration = Double.NaN;
        if (!timeBeforeVmMigration.isEmpty()) {
            meanTimeBeforeVmMigration = MathUtil.mean(timeBeforeVmMigration);
            stDevTimeBeforeVmMigration = MathUtil.stDev(timeBeforeVmMigration);
        }

        if (outputInCsv) {
            File folder = new File(outputFolder);
            if (!folder.exists()) {
                folder.mkdir();
            }
            File folder1 = new File(outputFolder + "/stats");
            if (!folder1.exists()) {
                folder1.mkdir();
            }
            File folder2 = new File(outputFolder + "/time_before_host_shutdown");
            if (!folder2.exists()) {
                folder2.mkdir();
            }
            File folder3 = new File(outputFolder + "/time_before_vm_migration");
            if (!folder3.exists()) {
                folder3.mkdir();
            }
            File folder4 = new File(outputFolder + "/metrics");
            if (!folder4.exists()) {
                folder4.mkdir();
            }

            StringBuilder data = new StringBuilder();
            String delimeter = ",";

            data.append(experimentName + delimeter);
            data.append("experimentName");
            data.append(String.format("%d", numberOfHosts) + delimeter);
            data.append(String.format("%d", numberOfVms) + delimeter);
            data.append(String.format("%.2f", totalSimulationTime) + delimeter);
            data.append(String.format("%.5f", energy) + delimeter);
            data.append(String.format("%d", numberOfMigrations) + delimeter);
            data.append(String.format("%.10f", sla) + delimeter);
            data.append(String.format("%.10f", slaTimePerActiveHost) + delimeter);
            data.append(String.format("%.10f", slaDegradationDueToMigration) + delimeter);
            data.append(String.format("%.10f", slaOverall) + delimeter);
            data.append(String.format("%.10f", slaAverage) + delimeter);
            // data.append(String.format("%.5f", slaTimePerVmWithMigration) + delimeter);
            // data.append(String.format("%.5f", slaTimePerVmWithoutMigration) + delimeter);
            // data.append(String.format("%.5f", slaTimePerHost) + delimeter);
            data.append(String.format("%d", numberOfHostShutdowns) + delimeter);
            data.append(String.format("%.2f", meanTimeBeforeHostShutdown) + delimeter);
            data.append(String.format("%.2f", stDevTimeBeforeHostShutdown) + delimeter);
            data.append(String.format("%.2f", meanTimeBeforeVmMigration) + delimeter);
            data.append(String.format("%.2f", stDevTimeBeforeVmMigration) + delimeter);

            if (datacenter.getVmAllocationPolicy() instanceof PowerVmAllocationPolicyMigrationAbstract) {
                PowerVmAllocationPolicyMigrationAbstract vmAllocationPolicy = (PowerVmAllocationPolicyMigrationAbstract) datacenter.getVmAllocationPolicy();

                double executionTimeVmSelectionMean = MathUtil.mean(vmAllocationPolicy.getExecutionTimeHistoryVmSelection());
                double executionTimeVmSelectionStDev = MathUtil.stDev(vmAllocationPolicy.getExecutionTimeHistoryVmSelection());
                double executionTimeHostSelectionMean = MathUtil.mean(vmAllocationPolicy.getExecutionTimeHistoryHostSelection());
                double executionTimeHostSelectionStDev = MathUtil.stDev(vmAllocationPolicy.getExecutionTimeHistoryHostSelection());
                double executionTimeVmReallocationMean = MathUtil.mean(vmAllocationPolicy.getExecutionTimeHistoryVmReallocation());
                double executionTimeVmReallocationStDev = MathUtil.stDev(vmAllocationPolicy.getExecutionTimeHistoryVmReallocation());
                double executionTimeTotalMean = MathUtil.mean(vmAllocationPolicy.getExecutionTimeHistoryTotal());
                double executionTimeTotalStDev = MathUtil.stDev(vmAllocationPolicy.getExecutionTimeHistoryTotal());

                data.append(String.format("%.5f", executionTimeVmSelectionMean) + delimeter);
                data.append(String.format("%.5f", executionTimeVmSelectionStDev) + delimeter);
                data.append(String.format("%.5f", executionTimeHostSelectionMean) + delimeter);
                data.append(String.format("%.5f", executionTimeHostSelectionStDev) + delimeter);
                data.append(String.format("%.5f", executionTimeVmReallocationMean) + delimeter);
                data.append(String.format("%.5f", executionTimeVmReallocationStDev) + delimeter);
                data.append(String.format("%.5f", executionTimeTotalMean) + delimeter);
                data.append(String.format("%.5f", executionTimeTotalStDev) + delimeter);

                writeMetricHistory(hosts, vmAllocationPolicy, outputFolder + "/metrics/" + experimentName
                        + "_metric");
            }

            data.append("\n");

            writeDataRow(data.toString(), outputFolder + "/stats/" + experimentName + "_stats.csv");
            writeDataColumn(timeBeforeHostShutdown, outputFolder + "/time_before_host_shutdown/"
                    + experimentName + "_time_before_host_shutdown.csv");
            writeDataColumn(timeBeforeVmMigration, outputFolder + "/time_before_vm_migration/"
                    + experimentName + "_time_before_vm_migration.csv");

        } else {
            Log.setDisabled(false);
            Log.printLine();
            Log.printLine(String.format("Experiment name: " + experimentName));
            Log.printLine(String.format("Number of hosts: " + numberOfHosts));
            Log.printLine(String.format("Number of VMs: " + numberOfVms));
            Log.printLine(String.format("Total simulation time: %.2f sec", totalSimulationTime));
            Log.printLine(String.format("Energy consumption: %.2f kWh", energy));
            Log.printLine(String.format("Number of VM migrations: %d", numberOfMigrations));
            Log.printLine(String.format("SLA: %.5f%%", sla * 100));
            Log.printLine(String.format(
                    "SLA perf degradation due to migration: %.2f%%",
                    slaDegradationDueToMigration * 100));
            Log.printLine(String.format("SLA time per active host: %.2f%%", slaTimePerActiveHost * 100));
            Log.printLine(String.format("Overall SLA violation: %.2f%%", slaOverall * 100));
            Log.printLine(String.format("Average SLA violation: %.2f%%", slaAverage * 100));
            // Log.printLine(String.format("SLA time per VM with migration: %.2f%%",
            // slaTimePerVmWithMigration * 100));
            // Log.printLine(String.format("SLA time per VM without migration: %.2f%%",
            // slaTimePerVmWithoutMigration * 100));
            // Log.printLine(String.format("SLA time per host: %.2f%%", slaTimePerHost * 100));
            Log.printLine(String.format("Number of host shutdowns: %d", numberOfHostShutdowns));
            Log.printLine(String.format(
                    "Mean time before a host shutdown: %.2f sec",
                    meanTimeBeforeHostShutdown));
            Log.printLine(String.format(
                    "StDev time before a host shutdown: %.2f sec",
                    stDevTimeBeforeHostShutdown));
            Log.printLine(String.format(
                    "Mean time before a VM migration: %.2f sec",
                    meanTimeBeforeVmMigration));
            Log.printLine(String.format(
                    "StDev time before a VM migration: %.2f sec",
                    stDevTimeBeforeVmMigration));

            if (datacenter.getVmAllocationPolicy() instanceof PowerVmAllocationPolicyMigrationAbstract) {
                PowerVmAllocationPolicyMigrationAbstract vmAllocationPolicy = (PowerVmAllocationPolicyMigrationAbstract) datacenter.getVmAllocationPolicy();

                double executionTimeVmSelectionMean = MathUtil.mean(vmAllocationPolicy.getExecutionTimeHistoryVmSelection());
                double executionTimeVmSelectionStDev = MathUtil.stDev(vmAllocationPolicy.getExecutionTimeHistoryVmSelection());
                double executionTimeHostSelectionMean = MathUtil.mean(vmAllocationPolicy.getExecutionTimeHistoryHostSelection());
                double executionTimeHostSelectionStDev = MathUtil.stDev(vmAllocationPolicy.getExecutionTimeHistoryHostSelection());
                double executionTimeVmReallocationMean = MathUtil.mean(vmAllocationPolicy.getExecutionTimeHistoryVmReallocation());
                double executionTimeVmReallocationStDev = MathUtil.stDev(vmAllocationPolicy.getExecutionTimeHistoryVmReallocation());
                double executionTimeTotalMean = MathUtil.mean(vmAllocationPolicy.getExecutionTimeHistoryTotal());
                double executionTimeTotalStDev = MathUtil.stDev(vmAllocationPolicy.getExecutionTimeHistoryTotal());

                Log.printLine(String.format(
                        "Execution time - VM selection mean: %.5f sec",
                        executionTimeVmSelectionMean));
                Log.printLine(String.format(
                        "Execution time - VM selection stDev: %.5f sec",
                        executionTimeVmSelectionStDev));
                Log.printLine(String.format(
                        "Execution time - host selection mean: %.5f sec",
                        executionTimeHostSelectionMean));
                Log.printLine(String.format(
                        "Execution time - host selection stDev: %.5f sec",
                        executionTimeHostSelectionStDev));
                Log.printLine(String.format(
                        "Execution time - VM reallocation mean: %.5f sec",
                        executionTimeVmReallocationMean));
                Log.printLine(String.format(
                        "Execution time - VM reallocation stDev: %.5f sec",
                        executionTimeVmReallocationStDev));
                Log.printLine(String.format("Execution time - total mean: %.5f sec", executionTimeTotalMean));
                Log.printLine(String.format("Execution time - total stDev: %.5f sec", executionTimeTotalStDev));
            }
            Log.printLine();
        }

        Log.setDisabled(true);
    }

    /**
     * Gets the sla time per active host.
     * 
     * @param hosts the hosts
     * @return the sla time per active host
     */
    protected static double getSlaTimePerActiveHost(List<Host> hosts) {
        double slaViolationTimePerHost = 0;
        double totalTime = 0;

        for (Host _host : hosts) {
            HostDynamicWorkload host = (HostDynamicWorkload) _host;
            double previousTime = -1;
            double previousAllocated = 0;
            double previousRequested = 0;
            boolean previousIsActive = true;

            for (HostStateHistoryEntry entry : host.getStateHistory()) {
                if (previousTime != -1 && previousIsActive) {
                    double timeDiff = entry.getTime() - previousTime;
                    totalTime += timeDiff;
                    if (previousAllocated < previousRequested) {
                        slaViolationTimePerHost += timeDiff;
                    }
                }

                previousAllocated = entry.getAllocatedMips();
                previousRequested = entry.getRequestedMips();
                previousTime = entry.getTime();
                previousIsActive = entry.isActive();
            }
        }

        return slaViolationTimePerHost / totalTime;
    }

    /**
     * Gets the sla time per host.
     * 
     * @param hosts the hosts
     * @return the sla time per host
     */
    protected static double getSlaTimePerHost(List<Host> hosts) {
        double slaViolationTimePerHost = 0;
        double totalTime = 0;

        for (Host _host : hosts) {
            HostDynamicWorkload host = (HostDynamicWorkload) _host;
            double previousTime = -1;
            double previousAllocated = 0;
            double previousRequested = 0;

            for (HostStateHistoryEntry entry : host.getStateHistory()) {
                if (previousTime != -1) {
                    double timeDiff = entry.getTime() - previousTime;
                    totalTime += timeDiff;
                    if (previousAllocated < previousRequested) {
                        slaViolationTimePerHost += timeDiff;
                    }
                }

                previousAllocated = entry.getAllocatedMips();
                previousRequested = entry.getRequestedMips();
                previousTime = entry.getTime();
            }
        }

        return slaViolationTimePerHost / totalTime;
    }

    /**
     * Gets the sla metrics.
     * 
     * @param vms the vms
     * @return the sla metrics
     */
    protected static Map<String, Double> getSlaMetrics(List<Vm> vms) {
        Map<String, Double> metrics = new HashMap<String, Double>();
        List<Double> slaViolation = new LinkedList<Double>();
        double totalAllocated = 0;
        double totalRequested = 0;
        double totalUnderAllocatedDueToMigration = 0;

        for (Vm vm : vms) {
            double vmTotalAllocated = 0;
            double vmTotalRequested = 0;
            double vmUnderAllocatedDueToMigration = 0;
            double previousTime = -1;
            double previousAllocated = 0;
            double previousRequested = 0;
            boolean previousIsInMigration = false;

            for (VmStateHistoryEntry entry : vm.getStateHistory()) {
                if (previousTime != -1) {
                    double timeDiff = entry.getTime() - previousTime;
                    vmTotalAllocated += previousAllocated * timeDiff;
                    vmTotalRequested += previousRequested * timeDiff;

                    if (previousAllocated < previousRequested) {
                        slaViolation.add((previousRequested - previousAllocated) / previousRequested);
                        if (previousIsInMigration) {
                            vmUnderAllocatedDueToMigration += (previousRequested - previousAllocated)
                                    * timeDiff;
                        }
                    }
                }

                previousAllocated = entry.getAllocatedMips();
                previousRequested = entry.getRequestedMips();
                previousTime = entry.getTime();
                previousIsInMigration = entry.isInMigration();
            }

            totalAllocated += vmTotalAllocated;
            totalRequested += vmTotalRequested;
            totalUnderAllocatedDueToMigration += vmUnderAllocatedDueToMigration;
        }

        metrics.put("overall", (totalRequested - totalAllocated) / totalRequested);
        if (slaViolation.isEmpty()) {
            metrics.put("average", 0.);
        } else {
            metrics.put("average", MathUtil.mean(slaViolation));
        }
        metrics.put("underallocated_migration", totalUnderAllocatedDueToMigration / totalRequested);
        // metrics.put("sla_time_per_vm_with_migration", slaViolationTimePerVmWithMigration /
        // totalTime);
        // metrics.put("sla_time_per_vm_without_migration", slaViolationTimePerVmWithoutMigration /
        // totalTime);

        return metrics;
    }

    /**
     * Write data column.
     * 
     * @param data the data
     * @param outputPath the output path
     */
    public void writeDataColumn(List<? extends Number> data, String outputPath) {
        File file = new File(outputPath);
        try {
            file.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
            System.exit(0);
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (Number value : data) {
                writer.write(value.toString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Write data row.
     * 
     * @param data the data
     * @param outputPath the output path
     */
    public void writeDataRow(String data, String outputPath) {
        File file = new File(outputPath);
        try {
            file.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
            System.exit(0);
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Write metric history.
     * 
     * @param hosts the hosts
     * @param vmAllocationPolicy the vm allocation policy
     * @param outputPath the output path
     */
    public void writeMetricHistory(
            List<? extends Host> hosts,
            PowerVmAllocationPolicyMigrationAbstract vmAllocationPolicy,
            String outputPath) {
        // for (Host host : hosts) {
        for (int j = 0; j < 10; j++) {
            Host host = hosts.get(j);

            if (!vmAllocationPolicy.getTimeHistory().containsKey(host.getId())) {
                continue;
            }
            File file = new File(outputPath + "_" + host.getId() + ".csv");
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
                System.exit(0);
            }
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                List<Double> timeData = vmAllocationPolicy.getTimeHistory().get(host.getId());
                List<Double> utilizationData = vmAllocationPolicy.getUtilizationHistory().get(host.getId());
                List<Double> metricData = vmAllocationPolicy.getMetricHistory().get(host.getId());

                for (int i = 0; i < timeData.size(); i++) {
                    writer.write(String.format(
                            "%.2f,%.2f,%.2f\n",
                            timeData.get(i),
                            utilizationData.get(i),
                            metricData.get(i)));
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    /**
     * Prints the metric history.
     * 
     * @param hosts the hosts
     * @param vmAllocationPolicy the vm allocation policy
     */
    public void printMetricHistory(
            List<? extends Host> hosts,
            PowerVmAllocationPolicyMigrationAbstract vmAllocationPolicy) {
        for (int i = 0; i < 10; i++) {
            Host host = hosts.get(i);

            Log.printLine("Host #" + host.getId());
            Log.printLine("Time:");
            if (!vmAllocationPolicy.getTimeHistory().containsKey(host.getId())) {
                continue;
            }
            for (Double time : vmAllocationPolicy.getTimeHistory().get(host.getId())) {
                Log.format("%.2f, ", time);
            }
            Log.printLine();

            for (Double utilization : vmAllocationPolicy.getUtilizationHistory().get(host.getId())) {
                Log.format("%.2f, ", utilization);
            }
            Log.printLine();

            for (Double metric : vmAllocationPolicy.getMetricHistory().get(host.getId())) {
                Log.format("%.2f, ", metric);
            }
            Log.printLine();
        }
    }

    /**
     * Gets the times before host shutdown.
     * 
     * @param hosts the hosts
     * @return the times before host shutdown
     */
    public List<Double> getTimesBeforeHostShutdown(List<Host> hosts) {
        List<Double> timeBeforeShutdown = new LinkedList<Double>();
        for (Host host : hosts) {
            boolean previousIsActive = true;
            double lastTimeSwitchedOn = 0;
            for (HostStateHistoryEntry entry : ((HostDynamicWorkload) host).getStateHistory()) {
                if (previousIsActive == true && entry.isActive() == false) {
                    timeBeforeShutdown.add(entry.getTime() - lastTimeSwitchedOn);
                }
                if (previousIsActive == false && entry.isActive() == true) {
                    lastTimeSwitchedOn = entry.getTime();
                }
                previousIsActive = entry.isActive();
            }
        }
        return timeBeforeShutdown;
    }

    /**
     * Gets the times before vm migration.
     * 
     * @param vms the vms
     * @return the times before vm migration
     */
    public List<Double> getTimesBeforeVmMigration(List<Vm> vms) {
        List<Double> timeBeforeVmMigration = new LinkedList<Double>();
        for (Vm vm : vms) {
            boolean previousIsInMigration = false;
            double lastTimeMigrationFinished = 0;
            for (VmStateHistoryEntry entry : vm.getStateHistory()) {
                if (previousIsInMigration == true && entry.isInMigration() == false) {
                    timeBeforeVmMigration.add(entry.getTime() - lastTimeMigrationFinished);
                }
                if (previousIsInMigration == false && entry.isInMigration() == true) {
                    lastTimeMigrationFinished = entry.getTime();
                }
                previousIsInMigration = entry.isInMigration();
            }
        }
        return timeBeforeVmMigration;
    }
}
