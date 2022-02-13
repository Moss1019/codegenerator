import json

from rest_framework.decorators import api_view
from django.http import HttpResponse

from todo.data_store import agent_store as store
from todo.serializers import UUIDEncoder, UUIDDecoder, AgentSerializer


@api_view(['GET', 'DELETE'])
def handle_agent(request, agent_id):
    if request.method == 'GET':
        return get_agent_by_pk(request, agent_id)
    if request.method == 'DELETE':
        return delete_agent(request, agent_id)
    return HttpResponse(status=204)


def get_agent_by_pk(request, agent):
    try:
        agent = store.get_agent(agent)
        result = AgentSerializer().serialize(agent)
        return HttpResponse(json.dumps(result, cls=UUIDEncoder), content_type='application/json')
    except Exception as e:
        print(e)
        return HttpResponse(status=404)


def delete_agent(request, agent):
    if store.delete_agent(agent):
        return HttpResponse()
    else:
        return HttpResponse(status=404)


@api_view(['GET', 'POST'])
def handle_agents(request):
    if request.method == 'GET':
        return get_agents(request)
    if request.method == 'POST':
        return create_agent(request)
    if request.method == 'PUT':
        return update_agent(request)
    return HttpResponse(204)


def get_agents(request):
    agents = store.get_agents()
    result = [AgentSerializer().serialize(entry) for entry in agents]
    return HttpResponse(json.dumps(result, cls=UUIDEncoder), content_type='application/json')


def create_agent(request):
    serializer = AgentSerializer()
    obj = serializer.deserialize(request.body)
    agent = store.create_agent(obj)
    result = serializer.serialize(agent)
    return HttpResponse(json.dumps(result, cls=UUIDEncoder), content_type='application/json')


def update_agent(request):
    obj = AgentSerializer().deserialize(request.body)
    if store.update_agent(obj):
        return HttpResponse(True)
    else:
        return HttpResponse(status=404)


@api_view(['GET'])
def get_agent_by_username(request, username):
    agent = store.get_agent_by_username(username)
    if agent is None:
        return HttpResponse(status=404)
    result = AgentSerializer().serialize(agent)
    return HttpResponse(json.dumps(result), content_type='application/json')
