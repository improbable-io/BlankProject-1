using UnityEngine;
using System.Collections;
using Improbable.Player;
using Improbable.Unity.Visualizer;

public class TriggerStepBehaviour : MonoBehaviour
{
    [Require] private PlayerControlsWriter playerControlsWriter;

    void Update()
    {
        if (Input.GetKeyDown(KeyCode.Q))
        {
            playerControlsWriter.Update.TriggerTriggerStep().FinishAndSend();
        }
    }
}