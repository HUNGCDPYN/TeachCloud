/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.edu.just.network;

import java.util.ArrayList;
import java.util.List;
import jo.just.designer.Interpreter;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.distributions.UniformDistr;
import org.cloudbus.cloudsim.lists.VmList;
import org.cloudbus.cloudsim.network.datacenter.AppCloudlet;
import org.cloudbus.cloudsim.network.datacenter.NetDatacenterBroker;
import org.cloudbus.cloudsim.network.datacenter.NetworkCloudletSpaceSharedScheduler;
import org.cloudbus.cloudsim.network.datacenter.NetworkConstants;
import org.cloudbus.cloudsim.network.datacenter.NetworkVm;
import org.cloudbus.cloudsim.network.datacenter.WorkflowApp;

/**
 *
 * @author apple
 */
public class NetworkDatacenterBroker extends NetDatacenterBroker {

    private Interpreter interpreter;

    public NetworkDatacenterBroker(String name) throws Exception {
        super(name);
    }
int vid = 0;
    @Override
    protected void createVmsInDatacenterBase(int datacenterId) {

        // send as much vms as possible for this datacenter before trying the
        // next one
        int requestedVms = 0;

        // All host will have two VMs (assumption) VM is the minimum unit
        if (createvmflag) {
            CreateVMs(datacenterId);
            createvmflag = false;
        }

        // generate Application execution Requests
        for (int i = 0; i < 100; i++) {
            this.getAppCloudletList().add(
                    new WorkflowApp(AppCloudlet.APP_Workflow, NetworkConstants.currentAppId, 0, 0, getId()));
            NetworkConstants.currentAppId++;

        }
        int k = 0;

        // schedule the application on VMs
        for (AppCloudlet app : this.getAppCloudletList()) {

            List<Integer> vmids = new ArrayList<Integer>();
            int numVms = linkDC.getVmList().size();
            UniformDistr ufrnd = new UniformDistr(0, numVms, getVmList().size() - 1/*5*/);
            for (int i = 0; i < app.numbervm; i++) {

                int vmid = (int) ufrnd.sample();
                vmids.add(vid++ % getVmList().size());
//                vmids.add(vmid);

            }

            if (vmids != null) {
                if (!vmids.isEmpty()) {

                    app.createCloudletList(vmids);
                    for (int i = 0; i < app.numbervm; i++) {
                        app.clist.get(i).setUserId(getId());
                        appCloudletRecieved.put(app.appID, app.numbervm);
                        this.getCloudletSubmittedList().add(app.clist.get(i));
                        cloudletsSubmitted++;

                        // Sending cloudlet
                        sendNow(
                                getVmsToDatacentersMap().get(this.getVmList().get(0).getId()),
                                CloudSimTags.CLOUDLET_SUBMIT,
                                app.clist.get(i));
                    }
                    System.out.println("app" + (k++));
                }
            }

        }
        setAppCloudletList(new ArrayList<AppCloudlet>());
        if (NetworkConstants.iteration < 10) {

            NetworkConstants.iteration++;
            this.schedule(getId(), NetworkConstants.nexttime, CloudSimTags.NextCycle);
        }

        setVmsRequested(requestedVms);
        setVmsAcks(0);
    }

    public void setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public void CreateVMs(int datacenterId) {

        for (int i = 0; i < interpreter.netDatacenterBrokers.size(); i++) {
            if (interpreter.netDatacenterBrokers.get(i).getId() == getId()) {
                int size = interpreter.netDatacenterBrokers.get(i).getVmList().size();
                for (int j = 0; j < size; j++) {

                    NetworkVm vm = (NetworkVm) interpreter.netDatacenterBrokers.get(i).getVmList().get(j);
                    linkDC.processVmCreateNetwork(vm);
                    // add the VM to the vmList
//                    getVmList().add(vm);
                    getVmsToDatacentersMap().put(vm.getId(), datacenterId);
                    getVmsCreatedList().add(VmList.getById(getVmList(), vm.getId()));
                }
            }
        }
    }
}
