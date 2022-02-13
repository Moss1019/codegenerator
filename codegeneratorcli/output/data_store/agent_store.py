
from todo.models import Agent
from todo.models import Item
from uuid import UUID


def get_agent(agent_id):
    try:
        agent = Agent.objects.get(agent_id=agent_id)
        items = list(Item.objects.filter(agent=agent))
        agent.set_field('items', items)
        return agent
    except Exception as e:
        return None


def get_agents():
    agents = list(Agent.objects.all())
    for agent in agents:
        items = list(Item.objects.filter(agent=agent))
        agent.set_field('items', items)
    return list(agents)


def create_agent(new_agent):
    new_agent.save()
    return new_agent


def update_agent(agent):
    try:
        agent.save()
        return True
    except Exception as e:
        return False


def delete_agent(agent_id):
    try:
        agent = Agent.objects.get(agent_id=agent_id)
        agent.delete()
        return True
    except Exception as e:
        return False


def get_agent_by_username(username):
    try:
        agent = Agent.objects.get(username=username)
        items = list(Item.objects.filter(agent=agent))
        agent.set_field('items', items)
        return agent
    except Exception as e:
        return None

