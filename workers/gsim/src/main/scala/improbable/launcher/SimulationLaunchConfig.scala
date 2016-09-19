package improbable.launcher

import improbable.bridgesettings.{UnityClientBridgeSettings, UnityFSimBridgeSettings}
import improbable.dapi.LaunchConfig
import improbable.fapi.bridge.CompositeBridgeSettingsResolver
import improbable.papi.worldapp.WorldApp
import improbable.unity.fabric.engine.DownloadableUnityConstraintToEngineDescriptorResolver

/**
  * These are the engine startup configs.
  *
  * ManualWorkerStartup will not start an engines when you start the game.
  * AutomaticWorkerStartup will automatically spool up engines as you need them.
  */
object ManualWorkerStartup extends SimulationLaunchConfigWithApps(dynamicallySpoolUpWorkers = false)

object AutomaticWorkerStartup extends SimulationLaunchConfigWithApps(dynamicallySpoolUpWorkers = true)

/**
  * Use this class to specify the list of apps you want to run when the game starts.
  */
class SimulationLaunchConfigWithApps(dynamicallySpoolUpWorkers: Boolean) extends
  SimulationLaunchConfig(appsToStart = Seq.empty, dynamicallySpoolUpWorkers)

class SimulationLaunchConfig(appsToStart: Seq[Class[_ <: WorldApp]],
                             dynamicallySpoolUpWorkers: Boolean) extends LaunchConfig(
  appsToStart,
  dynamicallySpoolUpWorkers,
  DefaultBridgeSettingsResolver,
  DownloadableUnityConstraintToEngineDescriptorResolver)

object DefaultBridgeSettingsResolver extends CompositeBridgeSettingsResolver(
  UnityClientBridgeSettings,
  UnityFSimBridgeSettings
)
