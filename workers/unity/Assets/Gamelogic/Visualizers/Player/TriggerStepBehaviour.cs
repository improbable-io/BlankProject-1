using System.Collections.Generic;
using Improbable;
using UnityEngine;
using Improbable.Player;
using Improbable.Unity.Visualizer;
using UnityEngine.UI;

namespace Assets.Gamelogic.Visualizers.Player
{
    public class TriggerStepBehaviour : MonoBehaviour
    {
        [Require] private PlayerControlsWriter playerControlsWriter;
        public IDictionary<EntityId, int> TempExpenditures;
        public int SpendInterval = 100;
        public GameObject InputField;
        public PlayerInfoReader playerInfoReader;
        public EntitySelector entitySelector;

        void OnEnable()
        {
            TempExpenditures = new Dictionary<EntityId, int>();
            InputField = GameObject.Find("InputField");
            InputField.SetActive(false);
            playerInfoReader = GetComponent<PlayerInfoReader>();
            entitySelector = GetComponent<EntitySelector>();
            SetupButtons();
        }

        void SetupButtons()
        {
            Button DecButton = InputField.transform.GetChild(2).GetComponent<Button>();
            DecButton.onClick.AddListener(DecButtonPressed);
            Button IncButton = InputField.transform.GetChild(3).GetComponent<Button>();
            IncButton.onClick.AddListener(IncButtonPressed);
            Button NextStepButton = GameObject.Find("NextStepButton").GetComponent<Button>();
            NextStepButton.onClick.AddListener(NextStepButtonPressed);
        }

        void Update()
        {
            if (entitySelector)
            {
                if (entitySelector.CurrentSelection)
                {
                    InputField.SetActive(true);
                    Text SpendTextField = InputField.transform.GetChild(1).GetComponent<Text>();
                    SpendTextField.text = (TempExpenditures.ContainsKey(entitySelector.CurrentSelection.EntityId())) ? ("£ " + TempExpenditures[entitySelector.CurrentSelection.EntityId()]) : "£ 0";
                }
                else
                {
                    InputField.SetActive(false);
                }
            }

            if (Input.GetKeyDown(KeyCode.Q))
            {
                TriggerNextStep();
            }
        }

        void IncButtonPressed()
        {
            if (entitySelector.CurrentSelection)
            {
                int spendAmount = Mathf.Min(SpendInterval, playerInfoReader.Money - playerInfoReader.TempExpenses);
                if (!TempExpenditures.ContainsKey(entitySelector.CurrentSelection.EntityId()))
                {
                    TempExpenditures.Add(entitySelector.CurrentSelection.EntityId(), spendAmount);
                }
                else
                {
                    TempExpenditures[entitySelector.CurrentSelection.EntityId()] += spendAmount;
                }
                playerInfoReader.TempExpenses += spendAmount;
            }
        }

        void DecButtonPressed()
        {
            if (entitySelector.CurrentSelection)
            {
                int spendAmount = (TempExpenditures.ContainsKey(entitySelector.CurrentSelection.EntityId())) ? Mathf.Min(TempExpenditures[entitySelector.CurrentSelection.EntityId()], SpendInterval) : 0;
                if (TempExpenditures.ContainsKey(entitySelector.CurrentSelection.EntityId()))
                {
                    TempExpenditures[entitySelector.CurrentSelection.EntityId()] -= spendAmount;
                }
                playerInfoReader.TempExpenses -= spendAmount;
            }
        }

        void NextStepButtonPressed()
        {
            TriggerNextStep();
        }

        void TriggerNextStep()
        {
            playerControlsWriter.Update.TriggerStep(ConvertExpenditures()).FinishAndSend();
            TempExpenditures = new Dictionary<EntityId, int>();
            playerInfoReader.TempExpenses = 0;
        }

        global::Improbable.Collections.List<StepData.Expenditure> ConvertExpenditures()
        {
            global::Improbable.Collections.List <StepData.Expenditure> result = new global::Improbable.Collections.List<StepData.Expenditure>();
            foreach (var item in TempExpenditures)
            {
                result.Add(new StepData.Expenditure(item.Key, item.Value));
            }
            return result;
        }
    }
}
